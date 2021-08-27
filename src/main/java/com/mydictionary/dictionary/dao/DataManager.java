package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.Translations;
import com.mydictionary.dictionary.model.User;

import java.sql.SQLException;
import java.util.List;

public interface DataManager extends AutoCloseable{

    void save(Translations translations) throws SQLException;
    Translations readTranslationByWord(String word) throws SQLException;
    void deleteTranslation(int id) throws SQLException;


    static DataManager getInstance(){
        return PostgresDataManager.getInstance();
    }
}
