package com.mydictionary.dictionary.controller;

public enum PagePath {
    MAIN("WEB-INF/jsp/main.jsp"),
    SIGN_IN("WEB-INF/jsp/sign_in.jsp"),
    REGISTER("WEB-INF/jsp/registration.jsp"),
    RESTORE_PASSWORD("WEB-INF/jsp/restore_password.jsp"),
    USER_DICTIONARY("WEB-INF/jsp/user_dictionary.jsp"),
    ERROR("WEB-INF/jsp/exception.jsp"),
    START_PAGE("index.jsp");

    private final String path;

    PagePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
