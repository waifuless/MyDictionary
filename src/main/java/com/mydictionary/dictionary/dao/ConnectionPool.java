package com.mydictionary.dictionary.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool extends AutoCloseable {

    Connection getConnection() throws SQLException;

    static ConnectionPool getInstance(){
        return HikariConnectionPool.getInstance();
    }
}
