package com.mydictionary.dictionary.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementPreparator {

    static StatementPreparator getInstance() {
        return SimpleStatementPreparator.getInstance();
    }

    void prepare(PreparedStatement statement, Object param, Object... params) throws SQLException;
}
