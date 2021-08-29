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

import static com.mydictionary.dictionary.dao.DictionaryColumn.*;

public class PostgresDataManager implements DataManager {

    private final static int MINIMAL_INDEX_VALUE = 1;
    private final static String FIND_WORD_ID_QUERY =
            "SELECT word_id FROM dictionary WHERE word='%s' AND language='%s'";
    private final static String INSERT_WORD_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO dictionary(word, language) VALUES('%s', '%s') RETURNING word_id";
    private final static String FIND_TRANSLATION_ID_QUERY =
            "SELECT translation_id FROM translation WHERE origin_word_id=%d AND translation_variant_id=%d";
    private final static String INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY =
            "INSERT INTO translation(origin_word_id, translation_variant_id) VALUES(%d, %d) RETURNING translation_id";
    private final static String INSERT_TRANSLATION_TO_USER_VOCABULARY_QUERY =
            "INSERT INTO user_vocabulary(user_id, translation_id) VALUES(%d, %d) ON CONFLICT DO NOTHING";


    private final static String READ_WORD_TRANSLATIONS_QUERY =
            "SELECT translation FROM user_dictionary " +
                    "WHERE user_id=%d AND origin_word='%s' AND src_lang_code='%s' AND dest_lang_code='%s'";
    private final static String READ_ALL_TRANSLATIONS_QUERY =
            "SELECT origin_word, translation FROM user_dictionary " +
                    "WHERE user_id=%d AND src_lang_code='%s' AND dest_lang_code='%s'";
    private final static String DELETE_WORD_TRANSLATIONS_QUERY =
            "DELETE FROM user_dictionary " +
                    "WHERE user_id=%d AND origin_word='%s' AND translation='%s'" +
                    " AND src_lang_code='%s' AND dest_lang_code='%s'";

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
            try (Statement dataQuery = connection.createStatement()) {
                int origin_word_id = findWordId(dataQuery, properties.getOrigin_word(), properties.getSrc_lang_code());
                if(origin_word_id<MINIMAL_INDEX_VALUE){
                    origin_word_id = insertWord(dataQuery, properties.getOrigin_word(), properties.getSrc_lang_code());
                }
                int translation_variant_id;
                int translation_id;
                String dest_lang_code = properties.getDest_lang_code();
                for (String translation_variant : translations) {
                    translation_variant_id = findWordId(dataQuery, translation_variant, dest_lang_code);
                    if(translation_variant_id<MINIMAL_INDEX_VALUE){
                        translation_variant_id = insertWord(dataQuery, translation_variant, dest_lang_code);
                    }
                    translation_id = findTranslationId(dataQuery, origin_word_id, translation_variant_id);
                    if(translation_id<MINIMAL_INDEX_VALUE){
                        translation_id = insertTranslation(dataQuery, origin_word_id, translation_variant_id);
                    }
                    dataQuery.execute(String.format(INSERT_TRANSLATION_TO_USER_VOCABULARY_QUERY,
                            properties.getUser_id(), translation_id));
                }
            }
        }
    }

    @Override
    public List<String> readWordTranslations(PropertiesWithOriginWord properties) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                ResultSet resultSet = dataQuery.executeQuery(String.format(READ_WORD_TRANSLATIONS_QUERY,
                        properties.getUser_id(), properties.getOrigin_word(),
                        properties.getSrc_lang_code(), properties.getDest_lang_code()));
                List<String> translations = new ArrayList<>();
                while (resultSet.next()) {
                    translations.add(resultSet.getString(TRANSLATION.columnName));
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
                    key = resultSet.getString(ORIGIN_WORD.columnName);
                    if (wordWithTranslations.containsKey(key)) {
                        wordWithTranslations.get(key).add(resultSet.getString(TRANSLATION.columnName));
                    } else {
                        values = new ArrayList<>();
                        values.add(resultSet.getString(TRANSLATION.columnName));
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
    public void close() throws Exception {
        connectionPool.close();
    }

    /**
     * @return -1 if word do not exist
     */
    private int findWordId(Statement statement, String word, String language) throws SQLException{
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
            throws SQLException{
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
    private int insertWord(Statement statement, String word, String language) throws SQLException{
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
            throws SQLException{
        ResultSet resultSet;
        resultSet = statement.executeQuery(String.format(INSERT_TRANSLATION_AND_RETURN_IT_ID_QUERY,
                origin_word_id, translation_variant_id));
        resultSet.next();
        return resultSet.getInt(1);
    }

}
