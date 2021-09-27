package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.controller.PagePath;
import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;
import jakarta.servlet.http.HttpSession;

public class SignIn implements Command {

    private static volatile SignIn instance;

    private SignIn() {
    }

    public static SignIn getInstance() {
        if (instance == null) {
            synchronized (SignIn.class) {
                if (instance == null) {
                    instance = new SignIn();
                }
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserManager userManager = UserManager.getInstance();
        try {
            User user = userManager.findUserByEmailAndPassword(email, password);
            SignInUser(user, request);
            return new CommandResponse(false, PagePath.MAIN.getPath());
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, PagePath.ERROR.getPath());
        }
    }

    private void SignInUser(User user, CommandRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        session = request.createSession();
        session.setAttribute(UserSessionAttribute.USER_ID.name(), user.getUserId());
        session.setAttribute(UserSessionAttribute.USER_EMAIL.name(), user.getEmail());
        session.setAttribute(UserSessionAttribute.USER_ROLE.name(), user.getRole());
    }
}
