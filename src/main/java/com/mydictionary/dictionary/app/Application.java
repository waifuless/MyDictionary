package com.mydictionary.dictionary.app;

import com.mydictionary.dictionary.dao.AdminDao;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.TranslationElement;
import com.mydictionary.dictionary.model.Translations;
import com.mydictionary.dictionary.translation.Translator;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Alfa class for test the system. Should be deleted in complete application
 */
public class Application {

    private final static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws  Exception{
        try {
            String strToTranslate;
            List<String> translations;
            Translator translator = Translator.create();
            String answer;
            int []favoriteWordsIndexes;
            List<String> translationsToSave;
            DataManager dataManager = DataManager.getInstance();
            AdminDao adminDao = AdminDao.getInstance();
            final int USER_ID = 2;
            do {
                System.out.println("Введите слово для перевода:");
                strToTranslate = keyboard.readLine();
                translations = translator.translate(strToTranslate);
                System.out.println("Список возможных переводов:");
                for (int i = 0; i < translations.size(); i++) {
                    System.out.printf("%d)%s\n", i+1, translations.get(i));
                }
                System.out.println("Какие переводы вам понравились?");
                answer = keyboard.readLine();
                if(!answer.equals("")) {
                    favoriteWordsIndexes = Arrays.stream(answer.split("\\s+"))
                            .distinct()
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    translationsToSave = new ArrayList<>();
                    for (Integer favoriteWordIndex : favoriteWordsIndexes) {
                        translationsToSave.add(translations.get(favoriteWordIndex - 1));
                    }
                    if (!translationsToSave.isEmpty()) {
                        dataManager.save(new Translations(USER_ID, strToTranslate,
                                translationsToSave, "en", "ru"));
                    }
                }
                System.out.println("user_dictionary now:");
                for (TranslationElement translationElement : adminDao.findTranslationElementList()) {
                    System.out.println(translationElement);
                }
                System.out.println("Хотите продолжить?(1\\0)");
                answer = keyboard.readLine();
            } while (!answer.equals("0") && !answer.equalsIgnoreCase("n"));

        } catch (IOException ex) {
            System.out.println("Something gone wrong with IO");
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
