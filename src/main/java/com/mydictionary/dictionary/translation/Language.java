package com.mydictionary.dictionary.translation;

public enum Language {
    RUSSIAN("ru"),
    ENGLISH("en");

    final String code;

    Language(String code) {
        this.code = code;
    }
}
