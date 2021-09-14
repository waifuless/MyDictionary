package com.mydictionary.dictionary.exception;

public class UserNotFoundException extends RuntimeException {

    private final static String EXCEPTION_MCG = "User with email: \"%s\" not found. Or password is invalid";

    public UserNotFoundException(String userEmail) {
        super(String.format(EXCEPTION_MCG, userEmail));
    }
}
