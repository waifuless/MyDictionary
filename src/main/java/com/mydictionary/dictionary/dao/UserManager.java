package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.User;

import java.sql.SQLException;

public interface UserManager {

    static UserManager getInstance() {
        return PostgresUserManager.getInstance();
    }

    void save(User user) throws SQLException;

    boolean isUserExist(String email) throws SQLException;

    int findIndexByEmailAndPasswordHash(String email, String passwordHash) throws SQLException;

    void deleteUser(int id) throws SQLException;
}
