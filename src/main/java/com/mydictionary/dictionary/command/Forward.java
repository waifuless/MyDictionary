package com.mydictionary.dictionary.command;

public class Forward implements Command {

    @Override
    public CommandResponse execute(CommandRequest request) {
        //todo: make validation
        String path;
        switch (request.getParameter("to")) {
            case "registration":
                path = "WEB-INF/jsp/registration.jsp";
                break;
            case "signIn":
                path = "WEB-INF/jsp/sign_in.jsp";
                break;
            case "restorePassword":
                path = "WEB-INF/jsp/restore_password.jsp";
                break;
            default:
                request.setAttribute("errorMessage", "unknown forward location");
                path = "WEB-INF/jsp/FuckedUpException";
                break;
        }
        return new CommandResponse(false, path);
    }
}
