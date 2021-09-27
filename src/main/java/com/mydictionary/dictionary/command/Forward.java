package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.controller.PagePath;

public class Forward implements Command {

    private static volatile Forward instance;

    private Forward() {
    }

    public static Forward getInstance() {
        if (instance == null) {
            synchronized (Forward.class) {
                if (instance == null) {
                    instance = new Forward();
                }
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        //todo: make validation
        String path;
        switch (request.getParameter("to")) {
            case "registration":
                path = PagePath.REGISTER.getPath();
                break;
            case "signIn":
                path = PagePath.SIGN_IN.getPath();
                break;
            case "restorePassword":
                path = PagePath.RESTORE_PASSWORD.getPath();
                break;
            case "userDictionary":
                path = PagePath.USER_DICTIONARY.getPath();
                break;
            case "main":
                path = PagePath.MAIN.getPath();
                break;
            default:
                request.setAttribute("errorMessage", "unknown forward location");
                path = PagePath.ERROR.getPath();
                break;
        }
        return new CommandResponse(false, path);
    }
}
