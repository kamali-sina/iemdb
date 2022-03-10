package com.iemdb;

import exception.CommandException;
import main.User;
import manager.ErrorManager;
import manager.UserManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsps/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = request.getParameter("user_email");
        try {
            User user = UserManager.getUser(userEmail);
            UserManager.logInUser(user);
            response.sendRedirect("/");
        } catch (CommandException commandException) {
            ErrorManager.error(request, response, commandException.getMessage());
        }
    }
}
