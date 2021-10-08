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
            "SELECT word_id AS word_id FROM dictionary WHERE word=? AND language=?;";
    private final static String INSERT_WORD_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO dictionary(word, language) VALUES(?, ?) RETURNING word_id AS word_id;";
    private final static String FIND_TRANSLATION_ID_QUERY =
            "SELECT translation_id AS translation_id FROM translation_set WHERE origin_word_id=?" +
                    "AND translation_word_id=?;";
    private final static String INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO translation_set(origin_word_id, translation_word_id) VALUES(?, ?)" +
                    "RETURNING translation_id AS translation_id;";
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
    private final StatementPreparator preparator;

    private PostgresDataManager() {
        connectionPool = ConnectionPool.getInstance();
        preparator = StatementPreparator.getInstance();
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
                 PreparedStatement insertWordStatement = connection
                         .prepareStatement(INSERT_WORD_AND_RETURN_IT_ID_QUERY);
                 PreparedStatement findTranslationStatement = connection.prepareStatement(FIND_TRANSLATION_ID_QUERY);
                 PreparedStatement insertTranslationStatement = connection
                         .prepareStatement(INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY);
                 PreparedStatement insertTranslationToUserVocabularyStatement = connection
                         .prepareStatement(INSERT_TRANSLATION_TO_USER_VOCABULARY_QUERY)) {

                int originWordId = insertIfDoesNotExist(findWordStatement, insertWordStatement, WORD_ID_COLUMN,
                        properties.getOriginWord(), properties.getSrcLangCode());
                int translationWordId;
                int translationId;
                String destLangCode = properties.getDestLangCode();
                for (String translationWord : translations) {
                    translationWordId = insertIfDoesNotExist(findWordStatement, insertWordStatement, WORD_ID_COLUMN,
                            translationWord, destLangCode);
                    translationId = insertIfDoesNotExist(findTranslationStatement, insertTranslationStatement,
                            TRANSLATION_ID_COLUMN, originWordId, translationWordId);
                    insertData(insertTranslationToUserVocabularyStatement, properties.getUserId(), translationId);
                }
            }
        }
    }

    @Override
    public List<String> readWordTranslations(PropertiesWithOriginWord properties) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(READ_WORD_TRANSLATIONS_QUERY)) {
                statement.setInt(1, properties.getUserId());
                statement.setString(2, properties.getOriginWord());
                statement.setString(3, properties.getSrcLangCode());
                statement.setString(4, properties.getDestLangCode());
                ResultSet resultSet = statement.executeQuery();
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
            try (PreparedStatement statement = connection.prepareStatement(READ_ALL_TRANSLATIONS_QUERY)) {
                statement.setInt(1, properties.getUserId());
                statement.setString(2, properties.getSrcLangCode());
                statement.setString(3, properties.getDestLangCode());
                ResultSet resultSet = statement.executeQuery();
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
            try (PreparedStatement statement = connection.prepareStatement(DELETE_WORD_TRANSLATIONS_QUERY)) {
                statement.setInt(1, properties.getUserId());
                statement.setString(2, properties.getOriginWord());
                //translation is third parameter
                statement.setString(4, properties.getSrcLangCode());
                statement.setString(5, properties.getDestLangCode());
                //In loop delete rows which differ only in translation
                for (String translation : translations) {
                    statement.setString(3, translation);
                    statement.execute();
                }
            }
        }
    }

    @Override
    public void deleteUnusedWords() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(DELETE_UNUSED_WORDS_QUERY);
            }
        }
    }

    @Override
    public void close() throws Exception {
        connectionPool.close();
    }

    private int insertIfDoesNotExist(PreparedStatement findStatement, PreparedStatement insertStatement,
                                     String retrieveColumn, Object param, Object... params) throws SQLException {
        int originWordId = findDataId(findStatement, retrieveColumn, param, params);
        if (originWordId < MINIMAL_INDEX_VALUE) {
            originWordId = insertDataAndRetrieveIndex(insertStatement, retrieveColumn, param, params);
        }
        return originWordId;
    }

    /**
     * @return -1 if translation do not exist
     */
    private int findDataId(PreparedStatement preparedStatement, String retrieveColumn, Object param,
                           Object... params)
            throws SQLException {
        preparator.prepare(preparedStatement, param, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(retrieveColumn);
        }
        return -1;
    }

    /**
     * @return id of new word
     */
    private int insertDataAndRetrieveIndex(PreparedStatement preparedStatement, String retrieveColumn,
                                           Object param, Object... params)
            throws SQLException {
        preparator.prepare(preparedStatement, param, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(retrieveColumn);
    }

    private void insertData(PreparedStatement preparedStatement, Object param, Object... params)
            throws SQLException {
        preparator.prepare(preparedStatement, param, params);
        preparedStatement.execute();
    }
}
