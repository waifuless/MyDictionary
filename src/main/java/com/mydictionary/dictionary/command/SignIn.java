package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;
import jakarta.servlet.http.HttpSession;

public class SignIn implements Command {

    @Override
    public CommandResponse execute(CommandRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserManager userManager = UserManager.getInstance();
        try {
            User user = userManager.findUserByEmailAndPassword(email, password);
            SignInUser(user, request.createSession());
            return new CommandResponse(false, "WEB-INF/jsp/main.jsp");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, "WEB-INF/jsp/exception.jsp");
        }
    }

    private void SignInUser(User user, HttpSession session){
        session.setAttribute(UserSessionAttribute.USER_ID.name(), user.getUserId());
        session.setAttribute(UserSessionAttribute.USER_EMAIL.name(), user.getEmail());
        session.setAttribute(UserSessionAttribute.USER_ROLE.name(), user.getRole());
    }
}
