package com.mydictionary.dictionary.security;

public interface PasswordEncoder {

    static PasswordEncoder getInstance() {
        return BcryptWithLocalSaltPasswordEncoder.getInstance();
    }

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
