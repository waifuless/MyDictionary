package com.mydictionary.dictionary.model;

public class User {

    private int user_id;
    private String email;
    private String passw_hash;

    public User(){}

    public User(int user_id, String email, String passw_hash) {
        this.user_id = user_id;
        this.email = email;
        this.passw_hash = passw_hash;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassw_hash() {
        return passw_hash;
    }

    public void setPassw_hash(String passw_hash) {
        this.passw_hash = passw_hash;
    }
}
