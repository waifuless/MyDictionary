package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;

import java.util.Map;

public class SignIn implements Command{

    @Override
    public CommandResponse execute(Map<String, String[]> parameterMap) {
        String email = parameterMap.get("email")[0];
        String password = parameterMap.get("password")[0];
        UserManager userManager = UserManager.getInstance();
        try {
            User user = userManager.findUserByEmailAndPassword(email, password);
            return new CommandResponse(false, "WEB-INF/jsp/main.jsp");
        } catch (Exception ex) {
            return new CommandResponse(false, "WEB-INF/jsp/FuckedUpException.jsp?errorMessage="+ex.getMessage());
        }
    }
}
