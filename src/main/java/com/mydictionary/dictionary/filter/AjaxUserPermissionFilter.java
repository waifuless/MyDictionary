package com.mydictionary.dictionary.filter;

import com.mydictionary.dictionary.command_model.UserSessionAttribute;
import com.mydictionary.dictionary.model.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "AjaxUserPermissionFilter", urlPatterns = "/AjaxControllerServlet")
public class AjaxUserPermissionFilter implements Filter {

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

        if (userRole==null) {

            response.setStatus(401);
            try(PrintWriter writer = response.getWriter()) {
                writer.print("You should be authorized to use this function");
                writer.flush();
            }
            //response.sendError(401, "You should be authorized to use this function");
            return;
        }
        chain.doFilter(request, response);
    }
}
