package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.controller.PagePath;
import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.UserFactory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Register implements Command {

    private final static String ERROR_ATTRIBUTE_NAME = "registerErrors";
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
            Set<RegisterError> errorSet = validateParameters(userManager, email, password, passwordRepeat);
            if (errorSet.size() > 0) {
                if(email!=null) {
                    request.setAttribute("email", email);
                }
                request.setAttribute(ERROR_ATTRIBUTE_NAME, errorSet);
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

    private Set<RegisterError> validateParameters(UserManager userManager,
                                                  String email, String password, String passwordRepeat)
            throws SQLException {

        Set<RegisterError> errorSet = new HashSet<>();
        if (!isEMailValid(email)) {
            errorSet.add(RegisterError.EMAIL_INVALID);
        }
        if (userManager.isUserExist(email)) {
            errorSet.add(RegisterError.USER_WITH_EMAIL_ALREADY_EXISTS);
        }
        if (!isPasswordValid(password)) {
            errorSet.add(RegisterError.PASSWORD_NOT_MEET_THE_REQUIREMENTS);
        }
        if (!password.equals(passwordRepeat)) {
            errorSet.add(RegisterError.PASSWORDS_NOT_MATCH);
        }
        return errorSet;
    }

    public enum RegisterError {
        EMAIL_INVALID,
        USER_WITH_EMAIL_ALREADY_EXISTS,
        PASSWORD_NOT_MEET_THE_REQUIREMENTS,
        PASSWORDS_NOT_MATCH
    }
}
