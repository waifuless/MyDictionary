package com.mydictionary.dictionary.translation;

import java.io.IOException;
import java.util.List;

public interface Translator {

    static Translator getInstance() {
        return BingTranslator.getInstance();
    }

    List<String> translate(String textToTranslate, String srcLanguage, String destLanguage) throws IOException;
}
