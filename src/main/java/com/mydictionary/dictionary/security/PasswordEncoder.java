package com.mydictionary.dictionary.security;

public interface PasswordEncoder {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

    static PasswordEncoder getInstance(){
        return BcryptWithLocalSaltPasswordEncoder.getInstance();
    }
}
