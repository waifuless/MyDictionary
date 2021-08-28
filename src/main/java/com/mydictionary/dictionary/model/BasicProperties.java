package com.mydictionary.dictionary.model;

import java.util.List;

/**
 * Holder to all needed translations of one word
 */
//todo: DELETE
public class BasicProperties {

    protected int user_id;
    protected String src_lang_code;
    protected String dest_lang_code;

    public BasicProperties(){}

    public BasicProperties(int user_id, String src_lang_code, String dest_lang_code) {
        this.user_id = user_id;
        this.src_lang_code = src_lang_code;
        this.dest_lang_code = dest_lang_code;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getSrc_lang_code() {
        return src_lang_code;
    }

    public String getDest_lang_code() {
        return dest_lang_code;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setSrc_lang_code(String src_lang_code) {
        this.src_lang_code = src_lang_code;
    }

    public void setDest_lang_code(String dest_lang_code) {
        this.dest_lang_code = dest_lang_code;
    }
}
