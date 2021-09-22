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

        String[] arrToDelete = request.getParameterValues("arrToDelete[]");
        if (arrToDelete != null) {
            List<String> listToDelete = Arrays.asList(arrToDelete);
            LOG.info("list to delete: {}", listToDelete);
            if (!listToDelete.isEmpty()) {
                dataManager.deleteTranslations(properties, listToDelete);
            }
        }

        String[] arrToSave = request.getParameterValues("arrToSave[]");
        if (arrToSave != null) {
            List<String> listToSave = Arrays.asList(arrToSave);
            LOG.info("list to save: {}", listToSave);
            if (!listToSave.isEmpty()) {
                dataManager.save(properties, listToSave);
            }
        }
        String jsonAnswer = new Gson().toJson("Changes saved successfully");
        return new AjaxCommandResponse("application/json", jsonAnswer);
    }
}
