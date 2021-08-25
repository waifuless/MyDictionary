package com.mydictionary.dictionary.translation;

import java.io.*;
import java.util.List;

import com.squareup.okhttp.*;

public class BingTranslator implements Translator{

    private final static String BING_SUBSCRIPTION_KEY = System.getenv("BING_SUBSCRIPTION_KEY");
    private final static String LOCATION = "westeurope";

    private final LookUpParser lookUpParser;
    private final TranslateParser translateParser;

    BingTranslator() {
        lookUpParser = new LookUpParser();
        translateParser = new TranslateParser();
    }

    private final HttpUrl translateUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("api.cognitive.microsofttranslator.com")
            .addPathSegment("/translate")
            .addQueryParameter("api-version", "3.0")
            .addQueryParameter("from", "en")
            .addQueryParameter("to", "ru")
            .build();

    private final HttpUrl lookupUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("api.cognitive.microsofttranslator.com")
            .addPathSegment("/dictionary/lookup")
            .addQueryParameter("api-version", "3.0")
            .addQueryParameter("from", "en")
            .addQueryParameter("to", "ru")
            .build();

    // Instantiates the OkHttpClient.
    private final OkHttpClient client = new OkHttpClient();

    // This function performs a POST request.
    private String postRequest(HttpUrl url, String textToPost) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                String.format("[{\"Text\": \"%s\"}]", textToPost));
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", BING_SUBSCRIPTION_KEY)
                .addHeader("Ocp-Apim-Subscription-Region", LOCATION)
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public List<String> translate(String textToTranslate) throws IOException{
        String translateResponse = postRequest(translateUrl, textToTranslate);
        String lookupResponse = postRequest(lookupUrl, textToTranslate);
        String mainTranslate = translateParser.parse(translateResponse);
        List<String> listOfOtherTranslations = lookUpParser.parse(lookupResponse);
        listOfOtherTranslations.add(0, mainTranslate);
        return listOfOtherTranslations;
    }
}
