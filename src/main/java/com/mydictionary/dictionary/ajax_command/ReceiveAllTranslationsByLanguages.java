package com.mydictionary.dictionary.ajax_command;

import com.google.gson.Gson;
import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.exception.OperationNotSupportedException;
import com.mydictionary.dictionary.model.BasicProperties;
import com.mydictionary.dictionary.model.PropertiesWithOriginWord;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ReceiveAllTranslationsByLanguages implements AjaxCommand {

    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception{
        int userId = (Integer) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
        String originLanguage = request.getParameter("originLanguage");
        String translateLanguage = request.getParameter("translateLanguage");
        BasicProperties properties =
                new BasicProperties(userId, originLanguage, translateLanguage);
        Map<String, List<String>> userDictionary = dataManager.readAllTranslationsByProperties(properties);
        String answerJson = new Gson().toJson(userDictionary);
        return new AjaxCommandResponse("application/json",answerJson);
    }
}
