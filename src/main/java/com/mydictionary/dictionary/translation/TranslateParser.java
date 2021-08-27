package com.mydictionary.dictionary.translation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class TranslateParser {

    TranslateParser() {
    }

    public String parse(String jsonToParse) {
        return parse(new JsonParser().parse(jsonToParse));
    }

    public String parse(JsonElement jsonToParse) {
        JsonObject jsonObject = jsonToParse.getAsJsonArray().get(0).getAsJsonObject();
        JsonArray arrayOfTranslations = jsonObject.getAsJsonArray("translations");
        JsonObject translationObject = arrayOfTranslations.get(0).getAsJsonObject();
        return translationObject.get("text").getAsString();
    }
}
