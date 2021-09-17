package com.mydictionary.dictionary.controller;

import com.mydictionary.dictionary.command.*;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.command_model.CommandResponse;
import com.mydictionary.dictionary.exception.InvalidArgumentException;
import com.mydictionary.dictionary.exception.OperationNotSupportedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
            throws ServletException, IOException {
        try {
            String requestedCommand = request.getParameter("command");
            if (requestedCommand == null) {
                throw new InvalidArgumentException("command does not exist");
            }
            Command command = findCommandByName(requestedCommand);
            CommandResponse commandResponse = command.execute(new CommandRequest(request));
            if (commandResponse.isRedirect()) {
                response.sendRedirect(commandResponse.getPath());
            } else {
                request.getRequestDispatcher(commandResponse.getPath()).forward(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("WEB-INF/jsp/exception.jsp").forward(request, response);
        }
    }

    private Command findCommandByName(String name) {
        switch (name) {
            case "forward":
                return new Forward();
            case "register":
                return new Register();
            case "signIn":
                return new SignIn();
            case "signOut":
                return new SignOut();
            case "restorePassword":
                throw new OperationNotSupportedException("This function isn`t written yet");
            default:
                throw new InvalidArgumentException("requested command unknown");
        }
    }
}
