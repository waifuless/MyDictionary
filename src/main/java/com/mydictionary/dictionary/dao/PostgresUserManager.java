package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresUserManager implements UserManager{

    private final static String INSERT_USER_QUERY =
            "INSERT INTO app_user(email, passw_hash) VALUES('%s','%s')";
    private final static String IS_EXIST_USER_QUERY =
            "SELECT EXISTS(SELECT 1 FROM app_user WHERE email='%s')";
    private final static String FIND_INDEX_BY_EMAIL_AND_PASSW_HASH_QUERY =
            "SELECT user_id FROM app_user WHERE email='%s' AND passw_hash='%s'";
    private final static String DELETE_USER_BY_ID_QUERY =
            "DELETE FROM app_user WHERE user_id = %d";

    private static PostgresUserManager instance;

    private final ConnectionPool connectionPool;

    private PostgresUserManager(){
        connectionPool = ConnectionPool.getInstance();
    }

    public static synchronized PostgresUserManager getInstance(){
        if(instance == null){
            instance = new PostgresUserManager();
        }
        return instance;
    }

    @Override
    public void save(User user) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(String.format(INSERT_USER_QUERY,
                        user.getEmail(), user.getPassw_hash()));
            }
        }
    }

    @Override
    public boolean isUserExist(String email) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(String.format(IS_EXIST_USER_QUERY, email));
                ResultSet resultSet = dataQuery.getResultSet();
                resultSet.first();
                return resultSet.getBoolean(1);
            }
        }
    }

    /**
     * Method return index of user if it exists and password hash is true,
     * return -1 if user not found of password is not true.
     */
    @Override
    public int findIndexByEmailAndPasswHash(String email, String passw_hash) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(String.format(FIND_INDEX_BY_EMAIL_AND_PASSW_HASH_QUERY, email, passw_hash));
                ResultSet resultSet = dataQuery.getResultSet();
                if(resultSet.first()){
                    return resultSet.getInt(1);
                }
                return -1;
            }
        }
    }

    //todo: think about an opportunity of delete random user
    @Override
    public void deleteUser(int id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(String.format(DELETE_USER_BY_ID_QUERY, id));
            }
        }
    }
}
