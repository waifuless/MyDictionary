package com.mydictionary.dictionary.command;

import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;
import jakarta.servlet.http.HttpSession;

public class SignOut implements Command{

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            HttpSession session = request.getSession();
            if(!(session==null)){
                session.invalidate();
            }
            return new CommandResponse(true, "index.jsp");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, "WEB-INF/jsp/exception.jsp");
        }
    }
}
