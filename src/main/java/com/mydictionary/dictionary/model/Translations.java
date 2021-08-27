package com.mydictionary.dictionary.model;

import java.util.List;

/**
 * Holder to all needed translations of one word
 */
//todo: choose Translations of TranslationElement
public class Translations {

    private int user_id;
    private String origin_word;
    private List<String> translations;
    private String src_lang_code;
    private String dest_lang_code;

    public Translations(){}

    public Translations(int user_id, String origin_word, List<String> translations, String src_lang_code, String dest_lang_code) {
        this.user_id = user_id;
        this.origin_word = origin_word;
        this.translations = translations;
        this.src_lang_code = src_lang_code;
        this.dest_lang_code = dest_lang_code;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getOrigin_word() {
        return origin_word;
    }

    public List<String> getTranslations() {
        return translations;
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

    public void setOrigin_word(String origin_word) {
        this.origin_word = origin_word;
    }

    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }

    public void setSrc_lang_code(String src_lang_code) {
        this.src_lang_code = src_lang_code;
    }

    public void setDest_lang_code(String dest_lang_code) {
        this.dest_lang_code = dest_lang_code;
    }
}
