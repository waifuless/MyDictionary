package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendTranslations implements Command{

    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            PropertiesWithOriginWord properties = (PropertiesWithOriginWord)
                    request.getSession().getAttribute(UserSessionAttribute.LAST_TRANSLATE_PROPERTIES.name());
            List<String> userSavedTranslations = dataManager.readWordTranslations(properties);
            List<String> listOfChoices = Arrays.asList((String[])request.getAttribute("userChoice[]"));
            Map<String, Boolean> translationsWithStatus = new HashMap<>();
            request.setAttribute("translationsWithStatus", translationsWithStatus);

            return new CommandResponse(false, "WEB-INF/jsp/main.jsp");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, "WEB-INF/jsp/exception.jsp");
        }
    }
}
