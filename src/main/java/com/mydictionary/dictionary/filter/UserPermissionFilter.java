package com.mydictionary.dictionary.filter;

import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.model.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "UserPermissionFilter", urlPatterns = {"/ControllerServlet", "/AjaxControllerServlet"})
public class UserPermissionFilter implements Filter {

    private final static List<String> openForwardDirectories =
            Arrays.asList("registration", "signIn", "restorePassword");

    public void init(FilterConfig config) throws ServletException {
        Filter.super.init(config);
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        final Role userRole = (Role) request.getSession(false).getAttribute(UserSessionAttribute.USER_ROLE.name());

        if (Role.ADMIN.equals(userRole) || Role.USER.equals(userRole)) {
            chain.doFilter(req, res);
            return;
        }

        String command = request.getParameter("command");

        if (command.equals("autoSignIn")||command.equals("register")||command.equals("signIn")) {
            chain.doFilter(req, res);
            return;
        }

        if (command.equals("forward")) {
            String direction = request.getParameter("to");
            if (openForwardDirectories.contains(direction)) {
                chain.doFilter(req, res);
                return;
            }
        }

        redirectClientToSignIn(response);
    }

    private void redirectClientToSignIn(HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
