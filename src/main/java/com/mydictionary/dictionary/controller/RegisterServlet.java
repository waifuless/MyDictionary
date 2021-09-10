package com.mydictionary.dictionary.controller;

import com.mydictionary.dictionary.dao.UserManager;
import com.mydictionary.dictionary.model.User;
import com.mydictionary.dictionary.model.UserFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("passwordRepeat");
        UserManager userManager = UserManager.getInstance();
        try {
            if(userManager.isUserExist(email)|| !password.equals(passwordRepeat)){
                request.setAttribute("errorMessage", "User already exists or passwords not match");
                request.getRequestDispatcher("/jsp/FuckedUpException.jsp").forward(request, response);
            }
            User user = UserFactory.getInstance().createUser(-1, email, password);
            userManager.save(user);
            System.out.println(request.getContextPath());
            response.sendRedirect(request.getContextPath()+"/jsp/sign_in.jsp");
        } catch (Exception ex){
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/jsp/FuckedUpException.jsp").forward(request, response);
        }
    }
}
