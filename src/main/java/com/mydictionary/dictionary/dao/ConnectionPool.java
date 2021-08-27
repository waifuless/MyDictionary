package com.mydictionary.dictionary.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool extends AutoCloseable {

    void init();
    Connection getConnection() throws SQLException;

    static ConnectionPool create(){
        return new HikariConnectionPool();
    }
}
