package com.mydictionary.dictionary.ajax_command;

import com.mydictionary.dictionary.command.Command;
import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.exception.OperationNotSupportedException;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendChoices implements AjaxCommand {

    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception{
            PropertiesWithOriginWord properties = (PropertiesWithOriginWord)
                    request.getSession().getAttribute(UserSessionAttribute.LAST_TRANSLATE_PROPERTIES.name());
            List<String> userSavedTranslations = dataManager.readWordTranslations(properties);
            List<String> listOfChoices = Arrays.asList((String[])request.getAttribute("userChoice[]"));
            Map<String, Boolean> translationsWithStatus = new HashMap<>();
            request.setAttribute("translationsWithStatus", translationsWithStatus);

            throw new OperationNotSupportedException();
    }
}
