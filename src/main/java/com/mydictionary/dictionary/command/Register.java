package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.controller.PagePath;
import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.UserFactory;

import java.sql.SQLException;
import java.util.Set;
import java.util.regex.Pattern;

public class Register implements Command {

    public final static Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    public final static int MAX_EMAIL_LENGTH = 254;

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
            boolean valid = validateParametersAndSetErrors(request, userManager, email, password, passwordRepeat);
            if (!valid) {
                if(email!=null) {
                    request.setAttribute("email", email);
                }
                return new CommandResponse(false, PagePath.REGISTER.getPath());
            }
            userManager.save(UserFactory.getInstance().createUser(-1, email, password));
            return new CommandResponse(false, PagePath.SIGN_IN.getPath());
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "Exception while saving user");
            return new CommandResponse(false, PagePath.ERROR.getPath());
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8 && password.length() <= 256;
    }

    private boolean isEMailValid(String email) {
        return email != null && VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()
                && email.length() <= MAX_EMAIL_LENGTH;
    }

    private boolean validateParametersAndSetErrors(CommandRequest request, UserManager userManager,
                                                  String email, String password, String passwordRepeat)
            throws SQLException {

        boolean valid = true;
        if (!isEMailValid(email)) {
            valid = false;
            request.setAttribute(RegisterError.EMAIL_INVALID.name(), RegisterError.EMAIL_INVALID);
        }
        if (userManager.isUserExist(email)) {
            valid = false;
            request.setAttribute(RegisterError.EMAIL_INVALID.name(),
                    RegisterError.USER_WITH_EMAIL_ALREADY_EXISTS);
        }
        if (!isPasswordValid(password)) {
            valid = false;
            request.setAttribute(RegisterError.PASSWORD_INVALID.name(),
                    RegisterError.PASSWORD_INVALID);
        }
        if (!password.equals(passwordRepeat)) {
            valid = false;
            request.setAttribute(RegisterError.PASSWORDS_NOT_MATCH.name(), RegisterError.PASSWORDS_NOT_MATCH);
        }
        return valid;
    }

    private void setErrors(CommandRequest request, Set<RegisterError> errors){

    }

    public enum RegisterError {
        EMAIL_INVALID,
        USER_WITH_EMAIL_ALREADY_EXISTS,
        PASSWORD_INVALID,
        PASSWORDS_NOT_MATCH;
    }
}
