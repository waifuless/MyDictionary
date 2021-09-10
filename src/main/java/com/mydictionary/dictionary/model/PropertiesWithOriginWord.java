package com.mydictionary.dictionary.model;

import java.util.Locale;

/**
 * Origin word should always be lowered case and striped
 */
public class PropertiesWithOriginWord extends BasicProperties {

    private String originWord;

    public PropertiesWithOriginWord() {
    }

    public PropertiesWithOriginWord(int user_id, String src_lang_code, String dest_lang_code, String originWord) {
        super(user_id, src_lang_code, dest_lang_code);
        this.originWord = originWord.toLowerCase(Locale.ROOT);
    }

    public String getOriginWord() {
        return originWord;
    }

    public void setOriginWord(String originWord) {
        this.originWord = originWord.strip().toLowerCase(Locale.ROOT);
    }
}
