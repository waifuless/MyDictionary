package com.mydictionary.dictionary.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ToggleSignServlet", value = "/ToggleSignServlet")
public class ToggleSignServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedPage = request.getParameter("to");
        switch (requestedPage){
            case "sign_in":
                request.getRequestDispatcher("open/sign_in.jsp").forward(request, response);
            case "registration":
                request.getRequestDispatcher("open/registration.jsp").forward(request, response);
            default:
                request.getRequestDispatcher("open/FuckedUpException.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
