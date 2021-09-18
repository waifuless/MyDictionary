package com.mydictionary.dictionary.ajax_command;

import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.dao.DataManager;
import com.mydictionary.dictionary.exception.OperationNotSupportedException;

public class ReceiveAllTranslationsByLanguages implements AjaxCommand {

    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public AjaxCommandResponse execute(CommandRequest request) {

        throw new OperationNotSupportedException();
        //Map<String, List<String>> userDictionary = dataManager.readAllTranslationsByProperties();

        //return new CommandResponse(false, "WEB-INF/jsp/user_dictionary.jsp");
    }
}
