package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.Translations;
import com.mydictionary.dictionary.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresDataManager implements DataManager{

    private final static String INSERT_TRANSLATION_QUERY =
            "INSERT INTO user_dictionary VALUES(%d, '%s', '%s', '%s', '%s')";

    private static PostgresDataManager instance;

    private final ConnectionPool connectionPool;

    private PostgresDataManager(){
        connectionPool = ConnectionPool.getInstance();
    }

    public static synchronized PostgresDataManager getInstance(){
        if(instance == null){
            instance = new PostgresDataManager();
        }
        return instance;
    }

    @Override
    public void save(Translations translations) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                final int user_id = translations.getUser_id();
                final String word = translations.getOrigin_word();
                final String src_lang_code = translations.getSrc_lang_code();
                final String dest_lang_code = translations.getDest_lang_code();
                //In loop add new entries which differ only in translation
                for (String translation : translations.getTranslations()) {
                    dataQuery.execute(String.format(INSERT_TRANSLATION_QUERY,
                            user_id, word, translation, src_lang_code, dest_lang_code));
                }
            }
        }
    }

    @Override
    public Translations readTranslationByWord(String word) throws SQLException{
        return null;
    }

    @Override
    public void deleteTranslation(int id) throws SQLException{

    }

    @Override
    public void close() throws Exception {
        connectionPool.close();
    }
}
