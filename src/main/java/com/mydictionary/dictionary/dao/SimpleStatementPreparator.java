package com.mydictionary.dictionary.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleStatementPreparator implements StatementPreparator {

    private static volatile SimpleStatementPreparator instance;

    private SimpleStatementPreparator() {
    }

    public static SimpleStatementPreparator getInstance() {
        if (instance == null) {
            synchronized (SimpleStatementPreparator.class) {
                if (instance == null) {
                    instance = new SimpleStatementPreparator();
                }
            }
        }
        return instance;
    }

    public void prepare(PreparedStatement statement, Object param, Object... params) throws SQLException {
        statement.setObject(1, param);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 2, params[i]);
        }
    }

}
