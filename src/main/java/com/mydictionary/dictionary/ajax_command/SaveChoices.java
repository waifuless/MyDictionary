package com.mydictionary.dictionary.ajax_command;

import com.google.gson.Gson;
import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class SaveChoices implements AjaxCommand {

    private final static Logger LOG = LogManager.getLogger(SaveChoices.class);

    private static volatile SaveChoices instance;

    private final DataManager dataManager = DataManager.getInstance();

    private SaveChoices() {
    }

    public static SaveChoices getInstance() {
        if (instance == null) {
            synchronized (SaveChoices.class) {
                if (instance == null) {
                    instance = new SaveChoices();
                }
            }
        }
        return instance;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {

        PropertiesWithOriginWord properties = (PropertiesWithOriginWord)
                request.getSession().getAttribute(UserSessionAttribute.LAST_TRANSLATE_PROPERTIES.name());

        String [] translations = request.getParameterValues("translations[]");
        String action = request.getParameter("action");
        if (translations != null && translations.length != 0 && action != null && !action.isEmpty()) {
            List<String> listOfTranslations = Arrays.asList(translations);
            LOG.info("action: {} ,list of translations: {}", action, listOfTranslations);
            if (action.equals("save")) {
                dataManager.save(properties, listOfTranslations);
            } else {
                dataManager.deleteTranslations(properties, listOfTranslations);
            }
        }
        String jsonAnswer = new Gson().toJson("Changes saved successfully");
        return new AjaxCommandResponse("application/json", jsonAnswer);
    }
}
