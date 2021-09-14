package com.mydictionary.dictionary.command;

import java.util.Map;

public class Forward implements Command{

    @Override
    public CommandResponse execute(Map<String, String[]> parameterMap) {
        //todo: make validation
        String path;
        switch (parameterMap.get("to")[0]){
            case "registration":
                path = "WEB-INF/jsp/registration.jsp";
                break;
            case "signIn":
                path = "WEB-INF/jsp/sign_in.jsp";
                break;
            case "restorePassword":
                path = "WEB-INF/jsp/restore_password.jsp";
                break;
            default:
                path = "WEB-INF/jsp/FuckedUpException?errorMessage=unknown_forward_location";
        }
        return new CommandResponse(false, path);
    }
}
