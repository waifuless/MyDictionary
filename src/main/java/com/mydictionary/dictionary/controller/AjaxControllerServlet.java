package com.mydictionary.dictionary.controller;

import com.mydictionary.dictionary.ajax_command.AjaxCommand;
import com.mydictionary.dictionary.ajax_command.ReceiveAllTranslationsByLanguages;
import com.mydictionary.dictionary.ajax_command.SendChoices;
import com.mydictionary.dictionary.ajax_command.Translate;
import com.mydictionary.dictionary.command_model.AjaxCommandResponse;
import com.mydictionary.dictionary.command_model.CommandRequest;
import com.mydictionary.dictionary.exception.InvalidArgumentException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AjaxControllerServlet", value = "/AjaxControllerServlet")
public class AjaxControllerServlet extends HttpServlet {

    private final static Logger LOG = LogManager.getLogger(AjaxControllerServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestedCommand = request.getParameter("command");
            if (requestedCommand == null) {
                throw new InvalidArgumentException("command does not exist");
            }
            AjaxCommand command = findCommandByName(requestedCommand);
            AjaxCommandResponse commandResponse = command.execute(new CommandRequest(request));

            response.setContentType(commandResponse.getResponseType());
            try (PrintWriter writer = response.getWriter()) {
                writer.write(commandResponse.getResponse());
            }
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("WEB-INF/jsp/exception.jsp").forward(request, response);
        }
    }

    private AjaxCommand findCommandByName(String name) {
        switch (name) {
            case "translate":
                return Translate.getInstance();
            case "sendChoices":
                return SendChoices.getInstance();
            case "receiveAllTranslationsByLanguage":
                return ReceiveAllTranslationsByLanguages.getInstance();
            default:
                throw new InvalidArgumentException("requested command unknown");
        }
    }
}
