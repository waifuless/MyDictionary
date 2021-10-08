package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.exception.UserNotFoundException;
import com.mydictionary.dictionary.model.Role;
import com.mydictionary.dictionary.model.User;
import com.mydictionary.dictionary.model.UserFactory;
import com.mydictionary.dictionary.security.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresUserManager implements UserManager {

    private final static String USER_EXISTENCE_COLUMN = "user_existence";
    private final static String USER_ID_COLUMN = "user_id";
    private final static String PASSWORD_HASH_COLUMN = "password_hash";
    private final static String ROLE_COLUMN = "role";
    private final static String INSERT_USER_QUERY =
            "INSERT INTO app_user(email, passw_hash, role) VALUES(?,?,?)";
    private final static String IS_EXIST_USER_QUERY =
            "SELECT EXISTS(SELECT 1 FROM app_user WHERE email=?) AS user_existence";
    private final static String FIND_USER_BY_EMAIL_QUERY =
            "SELECT user_id as user_id, passw_hash as password_hash, role as role FROM app_user WHERE email=?";
    private final static String DELETE_USER_BY_ID_QUERY =
            "DELETE FROM app_user WHERE user_id = ?";

    private static volatile PostgresUserManager instance;

    private final ConnectionPool connectionPool;
    private final UserFactory userFactory;
    private final PasswordEncoder encoder;

    private PostgresUserManager() {
        connectionPool = ConnectionPool.getInstance();
        userFactory = UserFactory.getInstance();
        encoder = PasswordEncoder.getInstance();
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
            try (PreparedStatement statement = connection.prepareStatement(INSERT_USER_QUERY)) {
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPasswordHash());
                statement.setString(3, user.getRole().name());
                statement.execute();
            }
        }
    }

    @Override
    public boolean isUserExist(String email) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(IS_EXIST_USER_QUERY)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return resultSet.getBoolean(USER_EXISTENCE_COLUMN);
            }
        }
    }

    @Override
    public User findUserByEmailAndPassword(String email, String password) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL_QUERY)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String passwordHash = resultSet.getString(PASSWORD_HASH_COLUMN);
                    if (encoder.matches(password, passwordHash)) {
                        return userFactory.createUserWithoutPassword(resultSet.getInt(USER_ID_COLUMN),
                                email, Role.valueOf(resultSet.getString(ROLE_COLUMN)));
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
            try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID_QUERY)) {
                statement.setInt(1, id);
                statement.execute();
            }
        }
    }
}
