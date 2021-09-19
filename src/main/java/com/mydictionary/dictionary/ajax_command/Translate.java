package com.mydictionary.dictionary.ajax_command;

import com.google.gson.Gson;
import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;
import com.mydictionary.dictionary.translation.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translate implements AjaxCommand {

    private final static Logger LOG = LogManager.getLogger(Translate.class);

    private static volatile Translate instance;

    private final Translator translator = Translator.getInstance();
    private final DataManager dataManager = DataManager.getInstance();

    private Translate() {
    }

    public static Translate getInstance() {
        if (instance == null) {
            synchronized (Translate.class) {
                if (instance == null) {
                    instance = new Translate();
                }
            }
        }
        return instance;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        int userId = (Integer) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
        String originWord = request.getParameter("originWord");
        String originLanguage = request.getParameter("originLanguage");
        String translateLanguage = request.getParameter("translateLanguage");

        PropertiesWithOriginWord properties =
                new PropertiesWithOriginWord(userId, originLanguage, translateLanguage, originWord);
        request.getSession().setAttribute(UserSessionAttribute.LAST_TRANSLATE_PROPERTIES.name(), properties);

        LOG.info("userId: {}", userId);
        LOG.info("originWord: {}", originWord);
        LOG.info("originLanguage: {}", originLanguage);
        LOG.info("translateLanguage: {}", translateLanguage);
        List<String> listOfTranslations = translator.translate(originWord, originLanguage, translateLanguage);
        List<String> userSavedTranslations = dataManager.readWordTranslations(properties);

        Map<String, Boolean> translationsWithStatus = new HashMap<>();
        fillMapWithStatus(listOfTranslations, translationsWithStatus, userSavedTranslations);

        String jsonAnswer = new Gson().toJson(translationsWithStatus);
        LOG.info("answerJson: {}", jsonAnswer);
        return new AjaxCommandResponse("application/json", jsonAnswer);
    }

    private void fillMapWithStatus(List<String> listOfTranslations, Map<String, Boolean> mapToFill,
                                   List<String> userSavedTranslations) {
        if (userSavedTranslations == null || userSavedTranslations.isEmpty()) {
            for (String translation : listOfTranslations) {
                mapToFill.put(translation, false);
            }
            return;
        }
        for (String translation : listOfTranslations) {
            mapToFill.put(translation, userSavedTranslations.contains(translation));
        }
    }
}
