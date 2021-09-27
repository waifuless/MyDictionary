package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.controller.PagePath;
import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.UserFactory;

public class Register implements Command {

    private final static String ERROR_ATTRIBUTE_NAME =  "registerError";

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
            if(!isEMailValid(email)){
                request.setAttribute(ERROR_ATTRIBUTE_NAME, RegisterError.EMAIL_INVALID);
                return new CommandResponse(false, PagePath.REGISTER.getPath());
            }
            if (userManager.isUserExist(email)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, RegisterError.USER_WITH_EMAIL_ALREADY_EXISTS);
                return new CommandResponse(false, PagePath.REGISTER.getPath());
            }
            if(!isPasswordValid(password)){
                request.setAttribute(ERROR_ATTRIBUTE_NAME, RegisterError.PASSWORD_NOT_MEET_THE_REQUIREMENTS);
                return new CommandResponse(false, PagePath.REGISTER.getPath());
            }
            if (!password.equals(passwordRepeat)){
                request.setAttribute(ERROR_ATTRIBUTE_NAME, RegisterError.PASSWORDS_NOT_MATCH);
                return new CommandResponse(false, PagePath.REGISTER.getPath());
            }
            userManager.save(UserFactory.getInstance().createUser(-1, email, password));
            return new CommandResponse(false, PagePath.SIGN_IN.getPath());
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "Exception while saving user");
            return new CommandResponse(false, PagePath.ERROR.getPath());
        }
    }

    private boolean isPasswordValid(String password){

        return true;
    }

    private boolean isEMailValid(String email){

        return true;
    }

    public enum RegisterError{
        EMAIL_INVALID,
        USER_WITH_EMAIL_ALREADY_EXISTS,
        PASSWORD_NOT_MEET_THE_REQUIREMENTS,
        PASSWORDS_NOT_MATCH
    }
}
