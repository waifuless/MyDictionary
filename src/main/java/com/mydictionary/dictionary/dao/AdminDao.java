package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.TranslationElement;
import com.mydictionary.dictionary.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Alfa class for test the system. Should be rewritten to use in complete application
 */
public class AdminDao implements AutoCloseable {

    private static AdminDao instance;

    private final ConnectionPool connectionPool;

    private AdminDao(){
        connectionPool = ConnectionPool.getInstance();
    }

    public static AdminDao getInstance(){
        if(instance == null){
            instance = new AdminDao();
        }
        return instance;
    }

    public List<User> findUsersList(){
        List<User> userList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                ResultSet resultSet = dataQuery.executeQuery("SELECT * FROM app_user");
                User newUser;
                while(resultSet.next()){
                    newUser = new User();
                    newUser.setUser_id(resultSet.getInt(1));
                    newUser.setEmail(resultSet.getString(2));
                    newUser.setPassw_hash(resultSet.getString(3));
                    userList.add(newUser);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }

    public List<TranslationElement> findTranslationElementList(){
        List<TranslationElement> translationsList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                ResultSet resultSet = dataQuery.executeQuery("SELECT * FROM user_dictionary");
                TranslationElement newTranslationElement;
                while(resultSet.next()){
                    newTranslationElement = new TranslationElement();
                    newTranslationElement.setUser_id(resultSet.getInt(1));
                    newTranslationElement.setOrigin_word(resultSet.getString(2));
                    newTranslationElement.setTranslation(resultSet.getString(3));
                    newTranslationElement.setSrc_lang_code(resultSet.getString(4));
                    newTranslationElement.setDest_lang_code(resultSet.getString(5));
                    translationsList.add(newTranslationElement);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return translationsList;
    }

    @Override
    public void close() throws Exception {
        connectionPool.close();
    }
}
