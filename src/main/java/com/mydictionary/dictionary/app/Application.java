package com.mydictionary.dictionary.app;

import com.mydictionary.dictionary.translation.Language;
import com.mydictionary.dictionary.translation.Translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Application {

    private final static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        try {
            String strToTranslate;
            List<String> translations;
            Translator translator = Translator.create(Language.RUSSIAN, Language.ENGLISH);
            String answer;
            do {
                System.out.println("Введите слово для перевода:");
                strToTranslate = keyboard.readLine();
                translations = translator.translate(strToTranslate);
                System.out.println("Список возможных переводов:");
                for (String translation : translations) {
                    System.out.println(translation);
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
