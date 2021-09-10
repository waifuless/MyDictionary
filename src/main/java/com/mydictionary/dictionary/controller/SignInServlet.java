package com.mydictionary.dictionary.controller;

import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/SignInServlet")
public class SignInServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserManager userManager = UserManager.getInstance();
        try {
            User user = userManager.findUserByEmailAndPassword(email, password);
            request.setAttribute("email", user.getEmail());
            request.setAttribute("userId", user.getUserId());
            request.setAttribute("role", user.getRole());
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
        }catch (Exception ex){
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/jsp/FuckedUpException.jsp").forward(request, response);
        }
    }
}
