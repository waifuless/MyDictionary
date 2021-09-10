package com.mydictionary.dictionary.app;

import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Alfa class for test the system. Should be deleted in complete application
 */
public class Application {

    private final static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
    private final static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        /*
        try {
            LOG.info("BLABLA");
            String strToTranslate;
            List<String> translations;
            Translator translator = Translator.create();
            String answer;
            int[] favoriteWordsIndexes;
            List<String> translationsToSave;
            DataManager dataManager = DataManager.getInstance();
            PropertiesWithOriginWord properties = new PropertiesWithOriginWord();
            final int USER_ID = 2;
            properties.setUserId(USER_ID);
            properties.setSrcLangCode("en");
            properties.setDestLangCode("ru");

            do {
                System.out.println("Введите слово для перевода:");
                strToTranslate = keyboard.readLine();
                translations = translator.translate(strToTranslate, properties.getSrcLangCode(), properties.getDestLangCode());
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
                    properties.setOriginWord(strToTranslate);
                    dataManager.save(properties, translationsToSave);
                }

                Map<String, List<String>> userWords = dataManager.readAllTranslationsByProperties(properties);
                for (Map.Entry<String, List<String>> entry : userWords.entrySet()) {
                    System.out.println(entry.getKey() + ":");
                    entry.getValue().forEach(str -> System.out.println("\t" + str));
                }


                System.out.println("Хотите продолжить?(1\\0)");
                answer = keyboard.readLine();
            } while (!answer.equals("0") && !answer.equalsIgnoreCase("n"));

            properties.setOrigin_word("dog");
            translations = new ArrayList<>();
            translations.add("собака");
            dataManager.deleteTranslations(properties, translations);

            //dataManager.deleteUnusedWords();
            //System.out.println(translations);
        } catch (Exception ex) {
            System.out.println("Something gone wrong");
            ex.printStackTrace();
            System.exit(-1);
        }

         */
        UserManager userManager = UserManager.getInstance();
        String email = "123";
        String password = "321";
        try {
            User user = userManager.findUserByEmailAndPassword(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex){
        }
    }
}
