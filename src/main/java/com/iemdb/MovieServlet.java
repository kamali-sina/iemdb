package com.iemdb;

import exception.CommandException;
import main.*;
import manager.ActorManager;
import manager.ErrorManager;
import manager.MovieManager;
import manager.UserManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

@WebServlet(name = "MovieServlet", value = "/movies/*")
public class MovieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UserManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String movieId = pathParts[1];
        try {
            MovieManager.getMovie(Integer.valueOf(movieId));
        } catch (CommandException commandException) {
            ErrorManager.error(request, response, commandException.getMessage());
        } catch (Exception exception) {
            ErrorManager.error(request, response, "Invalid value for movie_id");
        }
        request.getRequestDispatcher("/jsps/movie.jsp?movieId=" + movieId).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UserManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        String action = request.getParameter("action");

        switch (action) {
            case "rate" -> {
                try {
                    Integer movieId = Integer.valueOf(request.getParameter("movie_id"));
                    Integer quantity = Integer.valueOf(request.getParameter("quantity"));
                    Rating rating = new Rating(UserManager.getLoggedInUser().getEmail(), movieId, quantity);
                    MovieManager.addRating(rating);
                } catch (CommandException commandException) {
                    ErrorManager.error(request, response, commandException.getMessage());
                }
            }
            case "add" -> {
                try {
                    Integer movieId = Integer.valueOf(request.getParameter("movie_id"));
                    WatchListItem watchListItem = new WatchListItem(movieId, UserManager.getLoggedInUser().getEmail());
                    UserManager.addToWatchList(watchListItem);
                } catch (CommandException commandException) {
                    ErrorManager.error(request, response, commandException.getMessage());
                }

            }
            case "comment" -> {
                try {
                    String commentText = request.getParameter("comment");
                    Integer movieId = Integer.valueOf(request.getParameter("movie_id"));
                    Comment comment = new Comment(UserManager.getLoggedInUser().getEmail(), movieId, commentText);
                    MovieManager.addComment(comment);
                } catch (CommandException commandException) {
                    ErrorManager.error(request, response, commandException.getMessage());
                }
            }
            case "like" -> {
                try {
                    Integer commentId = Integer.valueOf(request.getParameter("comment_id"));
                    Vote vote = new Vote(UserManager.getLoggedInUser().getEmail(), commentId, 1);
                    UserManager.addVote(vote);
                } catch (CommandException commandException) {
                    ErrorManager.error(request, response, commandException.getMessage());
                }
            }
            case "dislike" -> {
                try {
                    Integer commentId = Integer.valueOf(request.getParameter("comment_id"));
                    Vote vote = new Vote(UserManager.getLoggedInUser().getEmail(), commentId, -1);
                    UserManager.addVote(vote);
                } catch (CommandException commandException) {
                    ErrorManager.error(request, response, commandException.getMessage());
                }
            }
        }
        response.sendRedirect("/movies/" + pathParts[1]);
    }
}
