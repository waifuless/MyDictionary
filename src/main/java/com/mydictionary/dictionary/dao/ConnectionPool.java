package com.mydictionary.dictionary.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool extends AutoCloseable {

    static ConnectionPool getInstance() {
        return HikariConnectionPool.getInstance();
    }

    Connection getConnection() throws SQLException;
}
