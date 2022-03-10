package com.iemdb;

import exception.CommandException;
import main.Movie;
import main.User;
import manager.ErrorManager;
import manager.MovieManager;
import manager.UserManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "WatchlistServlet", value = "/watchlist")
public class WatchlistServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UserManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        request.getRequestDispatcher("/jsps/watchlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UserManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String movie_id_string = request.getParameter("movie_id");
        try {
            Integer movie_id = Integer.valueOf(movie_id_string);
            Movie movie = MovieManager.getMovie(movie_id);
            UserManager.getLoggedInUser().removeFromWatchList(movie);
            response.sendRedirect("/watchlist");
        } catch (CommandException commandException) {
            ErrorManager.error(request, response, commandException.getMessage());
        } catch (Exception exception) {
            ErrorManager.error(request, response, "Invalid value for move_id");
        }
    }
}
