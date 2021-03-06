package com.mydictionary.dictionary.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionPool implements ConnectionPool {

    private static volatile HikariConnectionPool instance;

    private HikariDataSource dataSource;

    private HikariConnectionPool() {
        init();
    }

    public static HikariConnectionPool getInstance() {
        if (instance == null) {
            synchronized (HikariConnectionPool.class) {
                if (instance == null) {
                    instance = new HikariConnectionPool();
                }
            }
        }
        return instance;
    }

    private void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/dictionary");
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername("db_user");
        config.setPassword(System.getenv("DB_USER_PASSWORD"));
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        synchronized (this) {
            dataSource.close();
            instance = null;
        }
    }
}
