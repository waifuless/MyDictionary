package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.User;

import java.sql.SQLException;

public interface UserManager {

    void save(User user) throws SQLException;
    boolean isUserExist(String email) throws SQLException;
    int findIndexByEmailAndPasswHash(String email, String passw_hash) throws SQLException;
    void deleteUser(int id) throws SQLException;

    static UserManager getInstance(){
        return PostgresUserManager.getInstance();
    }
}
