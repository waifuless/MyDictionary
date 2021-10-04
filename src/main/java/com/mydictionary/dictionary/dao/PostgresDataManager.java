package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.BasicProperties;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgresDataManager implements DataManager {

    private final static int MINIMAL_INDEX_VALUE = 1;
    private final static String TRANSLATION_COLUMN = "translation";
    private final static String ORIGIN_WORD_COLUMN = "origin_word";
    private final static String WORD_ID_COLUMN = "word_id";
    private final static String TRANSLATION_ID_COLUMN = "translation_id";
    private final static String FIND_WORD_ID_QUERY =
            "SELECT word_id FROM dictionary WHERE word=? AND language=?;";
    private final static String INSERT_WORD_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO dictionary(word, language) VALUES(?, ?) RETURNING word_id;";
    private final static String FIND_TRANSLATION_ID_QUERY =
            "SELECT translation_id FROM translation_set WHERE origin_word_id=? AND translation_word_id=?;";
    private final static String INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO translation_set(origin_word_id, translation_word_id) VALUES(?, ?) RETURNING translation_id;";
    private final static String INSERT_TRANSLATION_TO_USER_VOCABULARY_QUERY =
            "INSERT INTO user_vocabulary(user_id, translation_id) VALUES(?, ?) ON CONFLICT DO NOTHING;";
    private final static String READ_WORD_TRANSLATIONS_QUERY =
            "SELECT translation_word.word AS translation FROM translation_set\n" +
                    "INNER JOIN dictionary AS origin_word ON translation_set.origin_word_id = origin_word.word_id\n" +
                    "INNER JOIN dictionary AS translation_word\n" +
                    "ON translation_set.translation_word_id = translation_word.word_id\n" +
                    "INNER JOIN user_vocabulary ON user_vocabulary.translation_id = translation_set.translation_id\n" +
                    "WHERE user_id=? AND origin_word.word=?\n" +
                    "AND origin_word.language=? AND translation_word.language=?;";
    private final static String READ_ALL_TRANSLATIONS_QUERY =
            "SELECT origin_word.word AS origin_word, translation_word.word AS translation FROM translation_set\n" +
                    "INNER JOIN dictionary AS origin_word ON translation_set.origin_word_id = origin_word.word_id\n" +
                    "INNER JOIN dictionary AS translation_word\n" +
                    "ON translation_set.translation_word_id = translation_word.word_id\n" +
                    "INNER JOIN user_vocabulary ON user_vocabulary.translation_id = translation_set.translation_id\n" +
                    "WHERE user_id = ? AND origin_word.language = ? AND translation_word.language = ?;";
    private final static String DELETE_WORD_TRANSLATIONS_QUERY =
            "DELETE FROM user_vocabulary\n" +
                    "USING translation_set,\n" +
                    "dictionary AS origin_word,\n" +
                    "dictionary AS translation_word\n" +
                    "WHERE translation_set.translation_id = user_vocabulary.translation_id\n" +
                    "AND origin_word.word_id = translation_set.origin_word_id\n" +
                    "AND translation_word.word_id = translation_set.translation_word_id\n" +
                    "AND user_id = ?\n" +
                    "AND origin_word.word = ?\n" +
                    "AND translation_word.word = ?\n" +
                    "AND origin_word.language = ?\n" +
                    "AND translation_word.language = ?;";
    private final static String DELETE_UNUSED_WORDS_QUERY =
            "DELETE FROM dictionary\n" +
                    "WHERE word_id NOT IN\n" +
                    "(SELECT DISTINCT(word_id) FROM dictionary\n" +
                    "INNER JOIN translation_set ON dictionary.word_id=translation_set.origin_word_id\n" +
                    "OR dictionary.word_id=translation_set.translation_word_id\n" +
                    "INNER JOIN user_vocabulary ON user_vocabulary.translation_id = translation_set.translation_id)";

    private static volatile PostgresDataManager instance;

    private final ConnectionPool connectionPool;

    private PostgresDataManager() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static PostgresDataManager getInstance() {
        if (instance == null) {
            synchronized (PostgresDataManager.class) {
                if (instance == null) {
                    instance = new PostgresDataManager();
                }
            }
        }
        return instance;
    }

    //todo:refactor
    @Override
    public void save(PropertiesWithOriginWord properties, List<String> translations)
            throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement findWordStatement = connection.prepareStatement(FIND_WORD_ID_QUERY);
            PreparedStatement insertWordStatement = connection.prepareStatement(INSERT_WORD_AND_RETURN_IT_ID_QUERY);
            PreparedStatement findTranslationStatement = connection.prepareStatement(FIND_TRANSLATION_ID_QUERY);
            PreparedStatement insertTranslationStatement = connection
                    .prepareStatement(INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY);
            PreparedStatement insertTranslationToUserStatement = connection
                    .prepareStatement(INSERT_TRANSLATION_TO_USER_VOCABULARY_QUERY)) {
                int originWordId = findWordId(findWordStatement, properties.getOriginWord(), properties.getSrcLangCode());
                if (originWordId < MINIMAL_INDEX_VALUE) {
                    originWordId = insertWord(insertWordStatement, properties.getOriginWord(), properties.getSrcLangCode());
                }
                int translationWordId;
                int translationId;
                String destLangCode = properties.getDestLangCode();
                for (String translationWord : translations) {
                    translationWordId = findWordId(findWordStatement, translationWord, destLangCode);
                    if (translationWordId < MINIMAL_INDEX_VALUE) {
                        translationWordId = insertWord(insertWordStatement, translationWord, destLangCode);
                    }
                    translationId = findTranslationId(findTranslationStatement, originWordId, translationWordId);
                    if (translationId < MINIMAL_INDEX_VALUE) {
                        translationId = insertTranslation(insertTranslationStatement, originWordId, translationWordId);
                    }
                    insertTranslationToUserStatement.setInt(1, properties.getUserId());
                    insertTranslationToUserStatement.setInt(2, translationId);
                    insertTranslationToUserStatement.execute();
                }
            }
        }
    }

    @Override
    public List<String> readWordTranslations(PropertiesWithOriginWord properties) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(String.format(READ_WORD_TRANSLATIONS_QUERY,
                        properties.getUserId(), properties.getOriginWord(),
                        properties.getSrcLangCode(), properties.getDestLangCode()));
                List<String> translations = new ArrayList<>();
                while (resultSet.next()) {
                    translations.add(resultSet.getString(TRANSLATION_COLUMN));
                }
                return translations;
            }
        }
    }

    @Override
    public Map<String, List<String>> readAllTranslationsByProperties(BasicProperties properties)
            throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement dataQuery = connection.prepareStatement(READ_ALL_TRANSLATIONS_QUERY)) {
                dataQuery.setInt(1, properties.getUserId());
                dataQuery.setString(2, properties.getSrcLangCode());
                dataQuery.setString(3, properties.getDestLangCode());
                ResultSet resultSet = dataQuery.executeQuery();
                Map<String, List<String>> wordWithTranslations = new HashMap<>();
                String key;
                List<String> values;
                while (resultSet.next()) {
                    key = resultSet.getString(ORIGIN_WORD_COLUMN);
                    if (wordWithTranslations.containsKey(key)) {
                        wordWithTranslations.get(key).add(resultSet.getString(TRANSLATION_COLUMN));
                    } else {
                        values = new ArrayList<>();
                        values.add(resultSet.getString(TRANSLATION_COLUMN));
                        wordWithTranslations.put(key, values);
                    }
                }
                return wordWithTranslations;
            }
        }
    }

    @Override
    public void deleteTranslations(PropertiesWithOriginWord properties, List<String> translations)
            throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                final int userId = properties.getUserId();
                final String word = properties.getOriginWord();
                final String srcLangCode = properties.getSrcLangCode();
                final String destLangCode = properties.getDestLangCode();
                //In loop delete rows which differ only in translation
                for (String translation : translations) {
                    dataQuery.execute(String.format(DELETE_WORD_TRANSLATIONS_QUERY,
                            userId, word, translation, srcLangCode, destLangCode));
                }
            }
        }
    }

    @Override
    public void deleteUnusedWords() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(DELETE_UNUSED_WORDS_QUERY);
            }
        }
    }

    @Override
    public void close() throws Exception {
        connectionPool.close();
    }

    /**
     * @return -1 if word do not exist
     */
    private int findWordId(PreparedStatement preparedStatement, String word, String language) throws SQLException {
        preparedStatement.setString(1, word);
        preparedStatement.setString(2, language);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(WORD_ID_COLUMN);
        }
        return -1;
    }

    /**
     * @return -1 if translation do not exist
     */
    private int findTranslationId(PreparedStatement preparedStatement, int originWordId, int translationWordId)
            throws SQLException {
        preparedStatement.setInt(1, originWordId);
        preparedStatement.setInt(2, translationWordId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(TRANSLATION_ID_COLUMN);
        }
        return -1;
    }

    /**
     * @return id of new word
     */
    private int insertWord(PreparedStatement preparedStatement, String word, String language) throws SQLException {
        preparedStatement.setString(1, word);
        preparedStatement.setString(2, language);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(WORD_ID_COLUMN);
    }

    /**
     * @return id of new translation
     */
    private int insertTranslation(PreparedStatement preparedStatement, int originWordId, int translationWordId)
            throws SQLException {
        setParametersToPreparedStatement(preparedStatement, originWordId, translationWordId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(TRANSLATION_ID_COLUMN);
    }

    private void setParametersToPreparedStatement(PreparedStatement preparedStatement, Object... parameters)
            throws SQLException{
        if(parameters!=null && preparedStatement!=null && parameters.length>0){
            for (int i = 1; i<parameters.length; i++) {
                preparedStatement.setObject(i+1, parameters[i]);
            }
        }
    }

}
