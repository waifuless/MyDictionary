package com.mydictionary.dictionary.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptWithLocalSaltPasswordEncoder implements PasswordEncoder{

    private final static String LOCAL_SALT = System.getenv("LOCAL_DICTIONARY_SALT");
    private static volatile BcryptWithLocalSaltPasswordEncoder instance;
    private final BCryptPasswordEncoder encoder;

    private BcryptWithLocalSaltPasswordEncoder(){
        encoder = new BCryptPasswordEncoder();
    }

    public static BcryptWithLocalSaltPasswordEncoder getInstance(){
        if(instance == null){
            synchronized (BcryptWithLocalSaltPasswordEncoder.class){
                if(instance==null){
                    instance= new BcryptWithLocalSaltPasswordEncoder();
                }
            }
        }
        return instance;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword+LOCAL_SALT);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword+LOCAL_SALT, encodedPassword);
    }
}
