package com.mydictionary.dictionary.translation;

import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.List;

public class BingTranslator implements Translator {

    private final static String BING_SUBSCRIPTION_KEY = System.getenv("BING_SUBSCRIPTION_KEY");
    private final static String LOCATION = "westeurope";

    private final LookUpParser lookUpParser;
    private final TranslateParser translateParser;
    private final HttpUrl translateUrl;
    private final HttpUrl lookupUrl;
    private final OkHttpClient client;

    BingTranslator() {
        lookUpParser = LookUpParser.getInstance();
        translateParser = TranslateParser.getInstance();
        client = new OkHttpClient();
        translateUrl = makeUrl(TranslateFunction.TRANSLATE, Language.ENGLISH, Language.RUSSIAN);
        lookupUrl = makeUrl(TranslateFunction.LOOKUP, Language.ENGLISH, Language.RUSSIAN);
    }

    BingTranslator(Language sourceLanguage, Language resultLanguage) {
        lookUpParser = LookUpParser.getInstance();
        translateParser = TranslateParser.getInstance();
        client = new OkHttpClient();
        translateUrl = makeUrl(TranslateFunction.TRANSLATE, sourceLanguage, resultLanguage);
        lookupUrl = makeUrl(TranslateFunction.LOOKUP, sourceLanguage, resultLanguage);
    }

    @Override
    public List<String> translate(String textToTranslate) throws IOException {
        String stripText = textToTranslate.strip();
        String translateResponse = postRequest(translateUrl, stripText);
        String lookupResponse = postRequest(lookupUrl, stripText);
        String mainTranslate = translateParser.parse(translateResponse);
        List<String> listOfOtherTranslations = lookUpParser.parse(lookupResponse);
        if (listOfOtherTranslations.stream().noneMatch(x -> x.equalsIgnoreCase(mainTranslate))) {
            listOfOtherTranslations.add(0, mainTranslate);
        }
        return listOfOtherTranslations;
    }

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

    private HttpUrl makeUrl(TranslateFunction translateFunction, Language sourceLanguage, Language resultLanguage) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment(translateFunction.functionPath)
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("from", sourceLanguage.code)
                .addQueryParameter("to", resultLanguage.code)
                .build();
    }

    private enum TranslateFunction {
        TRANSLATE("/translate"),
        LOOKUP("/dictionary/lookup");

        final String functionPath;

        TranslateFunction(String function) {
            functionPath = function;
        }
    }
}
