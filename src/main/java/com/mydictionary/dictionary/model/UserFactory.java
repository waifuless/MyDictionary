package com.mydictionary.dictionary.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserFactory {

    private final static String EMPTY_STRING = "";
    private static volatile UserFactory instance;
    private final BCryptPasswordEncoder encoder;

    private UserFactory(){
        encoder = new BCryptPasswordEncoder();
    }

    public static UserFactory getInstance(){
        if(instance==null){
            synchronized (UserFactory.class){
                if(instance==null){
                    instance=new UserFactory();
                }
            }
        }
        return instance;
    }

    public User createUserWithoutPassword(int user_id, String email, String role){
        return new User(user_id, email, EMPTY_STRING, role);
    }

    public User createUser(int user_id, String email, String password){
        return new User(user_id, email, encoder.encode(password));
    }

    public User createUser(int user_id, String email, String password, String role){
        return new User(user_id, email, encoder.encode(password), role);
    }
}
