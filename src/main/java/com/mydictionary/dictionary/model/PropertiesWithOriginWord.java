package com.mydictionary.dictionary.model;

public class PropertiesWithOriginWord extends BasicProperties {

    private String origin_word;

    public PropertiesWithOriginWord() {
    }

    public PropertiesWithOriginWord(int user_id, String src_lang_code, String dest_lang_code, String origin_word) {
        super(user_id, src_lang_code, dest_lang_code);
        this.origin_word = origin_word;
    }

    public String getOrigin_word() {
        return origin_word;
    }

    public void setOrigin_word(String origin_word) {
        this.origin_word = origin_word;
    }
}
