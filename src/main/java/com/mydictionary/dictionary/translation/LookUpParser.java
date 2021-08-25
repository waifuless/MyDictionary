package com.mydictionary.dictionary.translation;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class LookUpParser {

    private final static double MINIMAL_CONFIDENCE_VALUE = 0.1;

    LookUpParser() {
    }

    public List<String> parse(String jsonToParse) {
        return parse(new JsonParser().parse(jsonToParse));
    }

    public List<String> parse(JsonElement jsonToParse) {
        JsonObject jsonObject = jsonToParse.getAsJsonArray().get(0).getAsJsonObject();
        JsonArray arrayOfTranslations = jsonObject.getAsJsonArray("translations");
        List<String> result = new ArrayList<>();

        for (JsonElement translationElement : arrayOfTranslations) {

            JsonObject translationObject = translationElement.getAsJsonObject();
            if (translationObject.get("confidence").getAsDouble() >= MINIMAL_CONFIDENCE_VALUE) {
                result.add(translationObject.get("normalizedTarget").getAsString());
            }
        }
        return result;
    }

}
