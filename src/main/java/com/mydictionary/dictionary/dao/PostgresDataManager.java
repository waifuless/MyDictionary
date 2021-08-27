package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.Translations;
import com.mydictionary.dictionary.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PostgresDataManager implements DataManager{

    final String INSERT_USER_QUERY =
            "INSERT INTO app_user(email, passw_hash) VALUES('%s','%s')";
    final String INSERT_TRANSLATION_QUERY =
            "INSERT INTO user_dictionary VALUES(%d, '%s', '%s', '%s', '%s')";

    private static PostgresDataManager instance;

    private final ConnectionPool connectionPool;

    private PostgresDataManager(){
        connectionPool = ConnectionPool.create();
        connectionPool.init();
    }

    public static PostgresDataManager getInstance(){
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
    public void save(User user) throws SQLException{
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                    dataQuery.execute(String.format(INSERT_USER_QUERY,
                            user.getEmail(), user.getPassw_hash()));
            }
        }
    }

    @Override
    public User readUser(int id) throws SQLException{
        return null;
    }

    @Override
    public List<User> readAllUsers() throws SQLException{
        return null;
    }

    @Override
    public Translations readTranslationByWord(String word) throws SQLException{
        return null;
    }

    @Override
    public void deleteUser(int id) throws SQLException{

    }

    @Override
    public void deleteTranslation(int id) throws SQLException{

    }

    @Override
    public void close() throws Exception {
        connectionPool.close();
    }
}
