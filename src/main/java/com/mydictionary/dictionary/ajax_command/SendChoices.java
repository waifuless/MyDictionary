package com.mydictionary.dictionary.ajax_command;

import com.google.gson.Gson;
import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendChoices implements AjaxCommand {

    private final static Logger LOG = LogManager.getLogger(SendChoices.class);

    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        PropertiesWithOriginWord properties = (PropertiesWithOriginWord)
                request.getSession().getAttribute(UserSessionAttribute.LAST_TRANSLATE_PROPERTIES.name());
        List<String> userSavedTranslations = dataManager.readWordTranslations(properties);

        List<String> arrChecked = Arrays.asList(request.getParameterValues("arrChecked[]"));
        List<String> arrUnchecked = Arrays.asList(request.getParameterValues("arrUnchecked[]"));

        List<String> listToDelete = findTranslationsToDelete(userSavedTranslations, arrUnchecked);
        List<String> listToSave = findTranslationsToSave(userSavedTranslations, arrChecked);

        LOG.info("list to save: {}", listToSave);
        LOG.info("list to delete: {}", listToDelete);

        if (!listToDelete.isEmpty()) {
            dataManager.deleteTranslations(properties, listToDelete);
        }
        if (!listToSave.isEmpty()) {
            dataManager.save(properties, listToSave);
        }
        String jsonAnswer = new Gson().toJson("Your choices have saved");
        return new AjaxCommandResponse("application/json", jsonAnswer);
    }

    private List<String> findTranslationsToDelete(List<String> userSavedTranslations, List<String> arrUnchecked) {
        if (userSavedTranslations.isEmpty() || arrUnchecked.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<String> listToDelete = new ArrayList<>();
            for (String unchecked : arrUnchecked) {
                if (userSavedTranslations.contains(unchecked)) {
                    listToDelete.add(unchecked);
                }
            }
            return listToDelete;
        }
    }

    private List<String> findTranslationsToSave(List<String> userSavedTranslations, List<String> arrChecked) {
        if (userSavedTranslations.isEmpty() && !arrChecked.isEmpty()) {
            return arrChecked;
        }
        if (arrChecked.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> listToSave = new ArrayList<>();
        for (String checked : arrChecked) {
            if (!userSavedTranslations.contains(checked)) {
                listToSave.add(checked);
            }
        }
        return listToSave;
    }
}
