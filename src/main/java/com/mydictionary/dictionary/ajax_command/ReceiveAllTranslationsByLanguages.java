package com.mydictionary.dictionary.ajax_command;

import com.google.gson.Gson;
import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.model.BasicProperties;

import java.util.List;
import java.util.Map;

public class ReceiveAllTranslationsByLanguages implements AjaxCommand {

    private static volatile ReceiveAllTranslationsByLanguages instance;
    private final DataManager dataManager = DataManager.getInstance();

    private ReceiveAllTranslationsByLanguages() {
    }

    public static ReceiveAllTranslationsByLanguages getInstance() {
        if (instance == null) {
            synchronized (ReceiveAllTranslationsByLanguages.class) {
                if (instance == null) {
                    instance = new ReceiveAllTranslationsByLanguages();
                }
            }
        }
        return instance;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        int userId = (Integer) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
        String originLanguage = request.getParameter("originLanguage");
        String translateLanguage = request.getParameter("translateLanguage");
        BasicProperties properties =
                new BasicProperties(userId, originLanguage, translateLanguage);
        Map<String, List<String>> userDictionary = dataManager.readAllTranslationsByProperties(properties);
        String answerJson = new Gson().toJson(userDictionary);
        return new AjaxCommandResponse("application/json", answerJson);
    }
}
