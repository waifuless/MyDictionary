package com.mydictionary.dictionary.dao;

public enum DictionaryColumn {
    USER_ID("user_id"),
    ORIGIN_WORD("origin_word"),
    TRANSLATION("translation"),
    SRC_LANG_CODE("src_lang_code"),
    DEST_LANG_CODE("dest_lang_code");

    final String columnName;

    DictionaryColumn(String columnName){
        this.columnName = columnName;
    }
}
