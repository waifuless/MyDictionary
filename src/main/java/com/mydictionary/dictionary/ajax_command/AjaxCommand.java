package com.mydictionary.dictionary.ajax_command;

import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;

public interface AjaxCommand {

    AjaxCommandResponse execute(CommandRequest request) throws Exception;
}
