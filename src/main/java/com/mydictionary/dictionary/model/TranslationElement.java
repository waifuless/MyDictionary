package com.mydictionary.dictionary.model;

//todo: choose Translations of TranslationElement
public class TranslationElement {

    private int user_id;
    private String origin_word;
    private String translation;
    private String src_lang_code;
    private String dest_lang_code;

    public TranslationElement() {
    }

    public TranslationElement(int user_id, String origin_word, String translation, String src_lang_code, String dest_lang_code) {
        this.user_id = user_id;
        this.origin_word = origin_word;
        this.translation = translation;
        this.src_lang_code = src_lang_code;
        this.dest_lang_code = dest_lang_code;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getOrigin_word() {
        return origin_word;
    }

    public void setOrigin_word(String origin_word) {
        this.origin_word = origin_word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getSrc_lang_code() {
        return src_lang_code;
    }

    public void setSrc_lang_code(String src_lang_code) {
        this.src_lang_code = src_lang_code;
    }

    public String getDest_lang_code() {
        return dest_lang_code;
    }

    public void setDest_lang_code(String dest_lang_code) {
        this.dest_lang_code = dest_lang_code;
    }

    @Override
    public String toString() {
        return "TranslationElement{" +
                "user_id=" + user_id +
                ", origin_word='" + origin_word + '\'' +
                ", translation='" + translation + '\'' +
                ", src_lang_code='" + src_lang_code + '\'' +
                ", dest_lang_code='" + dest_lang_code + '\'' +
                '}';
    }
}
