package com.mydictionary.dictionary.dao;

import com.mydictionary.dictionary.model.BasicProperties;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataManager extends AutoCloseable {

    static DataManager getInstance() {
        return PostgresDataManager.getInstance();
    }

    void save(PropertiesWithOriginWord properties, List<String> translations) throws SQLException;

    List<String> readWordTranslations(PropertiesWithOriginWord properties) throws SQLException;

    /**
     * readAllTranslationsByProperties
     *
     * @return Map, where key String - origin word,value List<String> - translations of that word
     */
    Map<String, List<String>> readAllTranslationsByProperties(BasicProperties basicProperties) throws SQLException;

    void deleteTranslations(PropertiesWithOriginWord properties,
                            List<String> translations) throws SQLException;

    void deleteUnusedWords() throws SQLException;
}
