package com.mydictionary.dictionary.app;

import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;
import com.mydictionary.dictionary.translation.Translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Alfa class for test the system. Should be deleted in complete application
 */
public class Application {

    private final static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        try {
            String strToTranslate;
            List<String> translations;
            Translator translator = Translator.create();
            String answer;
            int[] favoriteWordsIndexes;
            List<String> translationsToSave;
            DataManager dataManager = DataManager.getInstance();
            PropertiesWithOriginWord properties = new PropertiesWithOriginWord();
            final int USER_ID = 2;
            properties.setUser_id(USER_ID);
            properties.setSrc_lang_code("en");
            properties.setDest_lang_code("ru");
            /*
            do {
                System.out.println("Введите слово для перевода:");
                strToTranslate = keyboard.readLine();
                translations = translator.translate(strToTranslate);
                System.out.println("Список возможных переводов:");
                for (int i = 0; i < translations.size(); i++) {
                    System.out.printf("%d)%s\n", i + 1, translations.get(i));
                }
                System.out.println("Какие переводы вам понравились?");
                answer = keyboard.readLine();
                if (!answer.equals("")) {
                    favoriteWordsIndexes = Arrays.stream(answer.split("\\s+"))
                            .distinct()
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    translationsToSave = new ArrayList<>();
                    for (Integer favoriteWordIndex : favoriteWordsIndexes) {
                        translationsToSave.add(translations.get(favoriteWordIndex - 1));
                    }
                    properties.setOrigin_word(strToTranslate);
                    dataManager.save(properties, translationsToSave);
                }

                Map<String,List<String>> userWords = dataManager.readAllTranslationsByProperties(properties);
                for (Map.Entry<String, List<String>> entry : userWords.entrySet()) {
                    System.out.println(entry.getKey()+":");
                    entry.getValue().forEach(str -> System.out.println("\t"+str));
                }


                System.out.println("Хотите продолжить?(1\\0)");
                answer = keyboard.readLine();
            } while (!answer.equals("0") && !answer.equalsIgnoreCase("n"));
             */

            properties.setOrigin_word("dog");
            translations = new ArrayList<>();
            translations.add("собака");
            dataManager.deleteTranslations(properties, translations);
            //System.out.println(translations);
        } catch (Exception ex) {
            System.out.println("Something gone wrong");
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
