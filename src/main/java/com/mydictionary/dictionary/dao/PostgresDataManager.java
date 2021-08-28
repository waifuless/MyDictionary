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

    private final static String INSERT_TRANSLATION_QUERY =
            "INSERT INTO user_dictionary VALUES(%d, '%s', '%s', '%s', '%s')";
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

    @Override
    public void save(PropertiesWithOriginWord properties, List<String> translations)
            throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                final int user_id = properties.getUser_id();
                final String word = properties.getOrigin_word();
                final String src_lang_code = properties.getSrc_lang_code();
                final String dest_lang_code = properties.getDest_lang_code();
                //In loop add new rows which differ only in translation
                for (String translation : translations) {
                    dataQuery.execute(String.format(INSERT_TRANSLATION_QUERY,
                            user_id, word, translation, src_lang_code, dest_lang_code));
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
                while(resultSet.next()){
                    translations.add(resultSet.getString(1));
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
                while(resultSet.next()){
                    key = resultSet.getString(1);
                    if(wordWithTranslations.containsKey(key)){
                       wordWithTranslations.get(key).add(resultSet.getString(2));
                    }
                    else{
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

    public static synchronized PostgresDataManager getInstance() {
        if (instance == null) {
            instance = new PostgresDataManager();
        }
        return instance;
    }

    @Override
    public void close() throws Exception {
        connectionPool.close();
    }
}
