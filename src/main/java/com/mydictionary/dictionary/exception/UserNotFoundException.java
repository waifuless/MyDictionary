package com.mydictionary.dictionary.exception;

public class UserNotFoundException extends RuntimeException{

    private final static String EXCEPTION_MCG = "User with email: \"%s\" not found.";

    public UserNotFoundException(String userEmail){
        super(String.format(EXCEPTION_MCG, userEmail));
    }
}
