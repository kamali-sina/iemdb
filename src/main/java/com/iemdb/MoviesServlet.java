package com.iemdb;

import manager.MovieManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "MoviesServlet", value = "/movies")
public class MoviesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsps/movies.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "search" -> {
                MovieManager.setQuery(request.getParameter("search"));
            }
            case "clear" -> {
                MovieManager.clearQuery();
            }
            case "sort_by_imdb" -> {
                MovieManager.setFilter("sortByImdb");
            }
            case "sort_by_date" -> {
                MovieManager.setFilter("sortByDate");
            }
        }
        response.sendRedirect("/movies");
    }
}
