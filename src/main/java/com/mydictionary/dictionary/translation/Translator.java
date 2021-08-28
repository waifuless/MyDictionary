package com.mydictionary.dictionary.translation;

import java.io.IOException;
import java.util.List;

public interface Translator {

    static Translator create() {
        return new BingTranslator();
    }

    static Translator create(Language sourceLanguage, Language resultLanguage) {
        return new BingTranslator(sourceLanguage, resultLanguage);
    }

    List<String> translate(String textToTranslate) throws IOException;
}
