package com.iemdb;

import input.DataReader;
import manager.UserManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "")
public class HomeServlet extends HttpServlet {
    private static final String externalServicesUrl = "http://138.197.181.131:5000";
    private static boolean isDataLoaded = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isDataLoaded) {
            DataReader.readDataFromUrl(externalServicesUrl);
            isDataLoaded = true;
        }

        if (UserManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
        }
        else {
            request.getRequestDispatcher("/jsps/home.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
