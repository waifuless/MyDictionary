package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.model.Role;
import jakarta.servlet.http.HttpSession;

public class AutoSignIn implements Command {

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            HttpSession session = request.getSession();
            if (session != null) {
                Role role = (Role) session.getAttribute(UserSessionAttribute.USER_ROLE.name());
                if (Role.USER.equals(role) || Role.ADMIN.equals(role)) {
                    return new CommandResponse(false, "WEB-INF/jsp/main.jsp");
                }
            }
            return new CommandResponse(false, "WEB-INF/jsp/sign_in.jsp");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, "WEB-INF/jsp/exception.jsp");
        }
    }
}
