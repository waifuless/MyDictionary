package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.controller.PagePath;
import com.mydictionary.dictionary.model.Role;
import jakarta.servlet.http.HttpSession;

public class AutoSignIn implements Command {

    private static volatile AutoSignIn instance;

    private AutoSignIn() {
    }

    public static AutoSignIn getInstance() {
        if (instance == null) {
            synchronized (AutoSignIn.class) {
                if (instance == null) {
                    instance = new AutoSignIn();
                }
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            HttpSession session = request.getSession();
            if (session != null) {
                Role role = (Role) session.getAttribute(UserSessionAttribute.USER_ROLE.name());
                if (Role.USER.equals(role) || Role.ADMIN.equals(role)) {
                    return new CommandResponse(false, PagePath.MAIN.getPath());
                }
            }
            return new CommandResponse(false, PagePath.SIGN_IN.getPath());
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, PagePath.ERROR.getPath());
        }
    }
}
