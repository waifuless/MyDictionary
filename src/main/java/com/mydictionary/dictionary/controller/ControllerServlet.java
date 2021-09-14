package com.mydictionary.dictionary.controller;

import com.mydictionary.dictionary.command.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String requestedCommand = request.getParameter("command");
        if(requestedCommand==null){
            request.setAttribute("errorMessage", "command does not exist");
            sendExceptionPage(request, response);
        }
        Command command = null;
        switch (requestedCommand){
            case "forward":
                command = new Forward();
                break;
            case "register":
                command = new Register();
                break;
            case "signIn":
                command = new SignIn();
                break;
            case "restorePassword":
                request.getRequestDispatcher("WEB-INF/jsp/registration.jsp").forward(request, response);
                break;
            default:
                request.setAttribute("errorMessage", "requested command unknown");
                sendExceptionPage(request, response);
                break;
        }
        CommandResponse commandResponse = command.execute(new CommandRequest(request));
        if(commandResponse.isRedirect()){
            response.sendRedirect(commandResponse.getPath());
        }else{
            request.getRequestDispatcher(commandResponse.getPath()).forward(request, response);
        }
    }

    private void sendExceptionPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        request.getRequestDispatcher("WEB-INF/jsp/exception.jsp").forward(request, response);
    }
}
