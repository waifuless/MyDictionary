package com.mydictionary.dictionary.translation;

import java.io.IOException;
import java.util.List;

public interface Translator {

    List<String> translate(String textToTranslate) throws IOException;

    static Translator create(){
        return new BingTranslator();
    }
}
