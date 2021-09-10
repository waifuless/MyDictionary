package com.mydictionary.dictionary.translation;

import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BingTranslator implements Translator {

    private final static String BING_SUBSCRIPTION_KEY = System.getenv("BING_SUBSCRIPTION_KEY");
    private final static String LOCATION = "westeurope";

    private static volatile BingTranslator instance;

    private final LookUpParser lookUpParser;
    private final TranslateParser translateParser;
    private final OkHttpClient client;

    private BingTranslator() {
        lookUpParser = LookUpParser.getInstance();
        translateParser = TranslateParser.getInstance();
        client = new OkHttpClient();
    }

    public static BingTranslator getInstance(){
        if(instance==null){
            synchronized (BingTranslator.class){
                if(instance==null){
                    instance=new BingTranslator();
                }
            }
        }
        return instance;
    }

    @Override
    public List<String> translate(String textToTranslate, String srcLanguage, String destLanguage) throws IOException {
        String stripText = textToTranslate.strip().toLowerCase(Locale.ROOT);
        HttpUrl translateUrl = makeUrl(TranslateFunction.TRANSLATE, srcLanguage, destLanguage);
        HttpUrl lookupUrl = makeUrl(TranslateFunction.LOOKUP, srcLanguage, destLanguage);
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

    private HttpUrl makeUrl(TranslateFunction translateFunction, String sourceLanguage, String resultLanguage) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment(translateFunction.functionPath)
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("from", sourceLanguage)
                .addQueryParameter("to", resultLanguage)
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
