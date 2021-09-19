package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.UserFactory;

public class Register implements Command {

    private static volatile Register instance;

    private Register() {
    }

    public static Register getInstance() {
        if (instance == null) {
            synchronized (Register.class) {
                if (instance == null) {
                    instance = new Register();
                }
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("passwordRepeat");
        UserManager userManager = UserManager.getInstance();
        try {
            if (userManager.isUserExist(email) || !password.equals(passwordRepeat)) {
                request.setAttribute("errorMessage", "User already exists or passwords not match");
                return new CommandResponse(false,
                        "WEB-INF/jsp/exception.jsp");
            }
            userManager.save(UserFactory.getInstance().createUser(-1, email, password));
            return new CommandResponse(false, "WEB-INF/jsp/sign_in.jsp");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "Exception while saving user");
            return new CommandResponse(false,
                    "WEB-INF/jsp/exception.jsp");
        }
    }
}
