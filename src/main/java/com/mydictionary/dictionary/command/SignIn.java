package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;

import java.util.Map;

public class SignIn implements Command{

    @Override
    public CommandResponse execute(CommandRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserManager userManager = UserManager.getInstance();
        try {
            userManager.findUserByEmailAndPassword(email, password);
            return new CommandResponse(false, "WEB-INF/jsp/main.jsp");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, "WEB-INF/jsp/exception.jsp");
        }
    }
}
