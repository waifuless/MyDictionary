package com.mydictionary.dictionary.translation;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class LookUpParser {

    //todo: Should I do it singleton?
    LookUpParser(){}

    public List<String> parse(String jsonToParse){
        return parse(new JsonParser().parse(jsonToParse));
    }

    public List<String> parse(JsonElement jsonToParse){
        JsonObject jsonObject = jsonToParse.getAsJsonArray().get(0).getAsJsonObject();
        JsonArray arrayOfTranslations = jsonObject.getAsJsonArray("translations");
        List<String> result = new ArrayList<>();
        for (JsonElement translationElement : arrayOfTranslations) {
            JsonObject translationObject = translationElement.getAsJsonObject();
            result.add(translationObject.get("normalizedTarget").getAsString());
        }
        return result;
    }

}
