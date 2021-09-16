package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;
import com.mydictionary.dictionary.translation.Translator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translate implements Command {

    private final Translator translator = Translator.getInstance();
    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {

            String originWord = request.getParameter("originWord");
            String originLanguage = request.getParameter("originLanguage");
            String translateLanguage = request.getParameter("translateLanguage");
            int userId = (Integer) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
            PropertiesWithOriginWord properties =
                    new PropertiesWithOriginWord(userId, originWord, originLanguage, translateLanguage);

            List<String> listOfTranslations = translator.translate(originWord, originLanguage, translateLanguage);
            List<String> userSavedTranslations = dataManager.readWordTranslations(properties);

            Map<String, Boolean> translationsWithStatus = new HashMap<>();
            fillMapWithStatus(listOfTranslations, translationsWithStatus, userSavedTranslations);
            request.setAttribute("translationsWithStatus", translationsWithStatus);

            request.getSession().setAttribute(UserSessionAttribute.LAST_TRANSLATE_PROPERTIES.name(), properties);
            return new CommandResponse(false, "WEB-INF/jsp/main.jsp");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, "WEB-INF/jsp/exception.jsp");
        }
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
