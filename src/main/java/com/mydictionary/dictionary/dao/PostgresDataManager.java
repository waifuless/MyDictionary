package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.BasicProperties;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgresDataManager implements DataManager {

    private final static int MINIMAL_INDEX_VALUE = 1;
    private final static String FIND_WORD_ID_QUERY =
            "SELECT word_id FROM dictionary WHERE word='%s' AND language='%s';";
    private final static String INSERT_WORD_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO dictionary(word, language) VALUES('%s', '%s') RETURNING word_id;";
    private final static String FIND_TRANSLATION_ID_QUERY =
            "SELECT translation_id FROM translation_set WHERE origin_word_id=%d AND translation_word_id=%d;";
    private final static String INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO translation_set(origin_word_id, translation_word_id) VALUES(%d, %d) RETURNING translation_id;";
    private final static String INSERT_TRANSLATION_TO_USER_VOCABULARY_QUERY =
            "INSERT INTO user_vocabulary(user_id, translation_id) VALUES(%d, %d) ON CONFLICT DO NOTHING;";
    private final static String READ_WORD_TRANSLATIONS_QUERY =
            "SELECT translation_word.word FROM translation_set\n" +
                    "INNER JOIN dictionary AS origin_word ON translation_set.origin_word_id = origin_word.word_id\n" +
                    "INNER JOIN dictionary AS translation_word\n" +
                    "ON translation_set.translation_word_id = translation_word.word_id\n" +
                    "INNER JOIN user_vocabulary ON user_vocabulary.translation_id = translation_set.translation_id\n" +
                    "WHERE user_id=%d AND origin_word.word='%s'\n" +
                    "AND origin_word.language='%s' AND translation_word.language='%s';";
    private final static String READ_ALL_TRANSLATIONS_QUERY =
            "SELECT origin_word.word, translation_word.word FROM translation_set\n" +
                    "INNER JOIN dictionary AS origin_word ON translation_set.origin_word_id = origin_word.word_id\n" +
                    "INNER JOIN dictionary AS translation_word\n" +
                    "ON translation_set.translation_word_id = translation_word.word_id\n" +
                    "INNER JOIN user_vocabulary ON user_vocabulary.translation_id = translation_set.translation_id\n" +
                    "WHERE user_id=%d AND origin_word.language='%s' AND translation_word.language='%s';";
    private final static String DELETE_WORD_TRANSLATIONS_QUERY =
            "DELETE FROM user_vocabulary\n" +
                    "USING translation_set,\n" +
                    "dictionary AS origin_word,\n" +
                    "dictionary AS translation_word\n" +
                    "WHERE translation_set.translation_id = user_vocabulary.translation_id\n" +
                    "AND origin_word.word_id = translation_set.origin_word_id\n" +
                    "AND translation_word.word_id = translation_set.translation_word_id\n" +
                    "AND user_id = %d\n" +
                    "AND origin_word.word = '%s'\n" +
                    "AND translation_word.word = '%s'\n" +
                    "AND origin_word.language = '%s'\n" +
                    "AND translation_word.language = '%s';";
    private final static String DELETE_UNUSED_WORDS_QUERY =
            "DELETE FROM dictionary\n" +
                    "WHERE word_id NOT IN\n" +
                    "(SELECT DISTINCT(word_id) FROM dictionary\n" +
                    "INNER JOIN translation_set ON dictionary.word_id=translation_set.origin_word_id\n" +
                    "OR dictionary.word_id=translation_set.translation_word_id\n" +
                    "INNER JOIN user_vocabulary ON user_vocabulary.translation_id = translation_set.translation_id)";

    private static PostgresDataManager instance;

    private final ConnectionPool connectionPool;

    private PostgresDataManager() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static synchronized PostgresDataManager getInstance() {
        if (instance == null) {
            instance = new PostgresDataManager();
        }
        return instance;
    }

    //todo:refactor
    @Override
    public void save(PropertiesWithOriginWord properties, List<String> translations)
            throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                int origin_word_id = findWordId(statement, properties.getOrigin_word(), properties.getSrc_lang_code());
                if (origin_word_id < MINIMAL_INDEX_VALUE) {
                    origin_word_id = insertWord(statement, properties.getOrigin_word(), properties.getSrc_lang_code());
                }
                int translation_variant_id;
                int translation_id;
                String dest_lang_code = properties.getDest_lang_code();
                for (String translation_variant : translations) {
                    translation_variant_id = findWordId(statement, translation_variant, dest_lang_code);
                    if (translation_variant_id < MINIMAL_INDEX_VALUE) {
                        translation_variant_id = insertWord(statement, translation_variant, dest_lang_code);
                    }
                    translation_id = findTranslationId(statement, origin_word_id, translation_variant_id);
                    if (translation_id < MINIMAL_INDEX_VALUE) {
                        translation_id = insertTranslation(statement, origin_word_id, translation_variant_id);
                    }
                    statement.execute(String.format(INSERT_TRANSLATION_TO_USER_VOCABULARY_QUERY,
                            properties.getUser_id(), translation_id));
                }
            }
        }
    }

    @Override
    public List<String> readWordTranslations(PropertiesWithOriginWord properties) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(String.format(READ_WORD_TRANSLATIONS_QUERY,
                        properties.getUser_id(), properties.getOrigin_word(),
                        properties.getSrc_lang_code(), properties.getDest_lang_code()));
                List<String> translations = new ArrayList<>();
                final int translationIndexValue = 1;
                while (resultSet.next()) {
                    translations.add(resultSet.getString(translationIndexValue));
                }
                return translations;
            }
        }
    }

    @Override
    public Map<String, List<String>> readAllTranslationsByProperties(BasicProperties properties)
            throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                ResultSet resultSet = dataQuery.executeQuery(String.format(READ_ALL_TRANSLATIONS_QUERY,
                        properties.getUser_id(), properties.getSrc_lang_code(), properties.getDest_lang_code()));
                Map<String, List<String>> wordWithTranslations = new HashMap<>();
                String key;
                List<String> values;
                while (resultSet.next()) {
                    key = resultSet.getString(1);
                    if (wordWithTranslations.containsKey(key)) {
                        wordWithTranslations.get(key).add(resultSet.getString(2));
                    } else {
                        values = new ArrayList<>();
                        values.add(resultSet.getString(2));
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
                final int user_id = properties.getUser_id();
                final String word = properties.getOrigin_word();
                final String src_lang_code = properties.getSrc_lang_code();
                final String dest_lang_code = properties.getDest_lang_code();
                //In loop delete rows which differ only in translation
                for (String translation : translations) {
                    dataQuery.execute(String.format(DELETE_WORD_TRANSLATIONS_QUERY,
                            user_id, word, translation, src_lang_code, dest_lang_code));
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
    private int findWordId(Statement statement, String word, String language) throws SQLException {
        statement.execute(String.format(FIND_WORD_ID_QUERY, word, language));
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    /**
     * @return -1 if translation do not exist
     */
    private int findTranslationId(Statement statement, int origin_word_id, int translation_variant_id)
            throws SQLException {
        statement.execute(String.format(FIND_TRANSLATION_ID_QUERY, origin_word_id, translation_variant_id));
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    /**
     * @return id of new word
     */
    private int insertWord(Statement statement, String word, String language) throws SQLException {
        ResultSet resultSet;
        resultSet = statement.executeQuery(String.format(INSERT_WORD_AND_RETURN_IT_ID_QUERY,
                word, language));
        resultSet.next();
        return resultSet.getInt(1);
    }

    /**
     * @return id of new translation
     */
    private int insertTranslation(Statement statement, int origin_word_id, int translation_variant_id)
            throws SQLException {
        ResultSet resultSet;
        resultSet = statement.executeQuery(String.format(INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY,
                origin_word_id, translation_variant_id));
        resultSet.next();
        return resultSet.getInt(1);
    }

}
