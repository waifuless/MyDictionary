package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.exception.UserNotFoundException;
import com.mydictionary.dictionary.model.User;
import com.mydictionary.dictionary.model.UserFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresUserManager implements UserManager {

    private final static String USER_EXISTENCE_COLUMN = "user_existence";
    private final static String USER_ID_COLUMN = "user_id";
    private final static String PASSWORD_HASH_COLUMN = "passw_hash";
    private final static String ROLE_COLUMN = "role";
    private final static String INSERT_USER_QUERY =
            "INSERT INTO app_user(email, passw_hash, role) VALUES('%s','%s','%s')";
    private final static String IS_EXIST_USER_QUERY =
            "SELECT EXISTS(SELECT 1 FROM app_user WHERE email='%s') AS user_existence";
    private final static String FIND_USER_BY_EMAIL_QUERY =
            "SELECT user_id, passw_hash, role FROM app_user WHERE email='%s'";
    private final static String DELETE_USER_BY_ID_QUERY =
            "DELETE FROM app_user WHERE user_id = %d";

    private static volatile PostgresUserManager instance;

    private final ConnectionPool connectionPool;
    private final UserFactory userFactory;
    private final BCryptPasswordEncoder encoder;

    private PostgresUserManager() {
        connectionPool = ConnectionPool.getInstance();
        userFactory = UserFactory.getInstance();
        encoder = new BCryptPasswordEncoder();
    }

    public static PostgresUserManager getInstance() {
        if (instance == null) {
            synchronized (PostgresUserManager.class) {
                if (instance == null) {
                    instance = new PostgresUserManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void save(User user) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(String.format(INSERT_USER_QUERY,
                        user.getEmail(), user.getPasswordHash(), user.getRole()));
            }
        }
    }

    @Override
    public boolean isUserExist(String email) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(String.format(IS_EXIST_USER_QUERY, email));
                ResultSet resultSet = dataQuery.getResultSet();
                resultSet.next();
                return resultSet.getBoolean(USER_EXISTENCE_COLUMN);
            }
        }
    }

    @Override
    public User findUserByEmailAndPassword(String email, String password) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement dataQuery = connection.createStatement()) {
                dataQuery.execute(String.format(FIND_USER_BY_EMAIL_QUERY, email));
                ResultSet resultSet = dataQuery.getResultSet();
                if (resultSet.next()) {
                    String passwordHash = resultSet.getString(PASSWORD_HASH_COLUMN);
                    if (encoder.matches(password, passwordHash)) {
                        return userFactory.createUserWithoutPassword(resultSet.getInt(USER_ID_COLUMN),
                                email, resultSet.getString(ROLE_COLUMN));
                    }
                }
            }
        }
        throw new UserNotFoundException(email);
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
