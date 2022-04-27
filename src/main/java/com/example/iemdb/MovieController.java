package com.example.iemdb;

import exception.CommandException;
import exception.ErrorType;
import main.Comment;
import main.Movie;
import main.Rating;
import manager.ActorManager;
import manager.MovieManager;
import manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import output.Output;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/movies")
public class MovieController {

    @GetMapping("/")
    public Output getMovies(HttpServletResponse response) throws CommandException {
        try {
            return new Output(HttpStatus.OK.value(), MovieManager.movies.values());
        } catch (CommandException commandException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Output getMovie(HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        try {
            return new Output(HttpStatus.OK.value(), MovieManager.getMovie(id));
        } catch (CommandException commandException) {
            if (commandException.getErrorType().equals(ErrorType.MovieNotFound)) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new Output(HttpStatus.NOT_FOUND.value(), "The server can not find the movie.");
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
            }
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public Output getSearchResult(HttpServletResponse response, @RequestParam Map<String,String> allParams) throws CommandException {
        String filter = allParams.get("filter");
        String sortedBy = allParams.getOrDefault("sortedBy", "sortByImdb"); // sortByDate or sortByImdb

        if (!MovieManager.sortedBySet.contains(sortedBy)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), "Please provide a correct sortedBy argument");
        }
        try {
            switch (filter) {
                case "name" -> {
                    String searchValue = allParams.getOrDefault("searchValue", "");
                    ArrayList <Movie> movies = MovieManager.searchMovies(searchValue);
                    return new Output(HttpStatus.OK.value(), MovieManager.sortMovies(movies, sortedBy));
                }
                case "releaseYear" -> {
                    ArrayList <Movie> movies = MovieManager.getMoviesByReleaseYear(Integer.valueOf(allParams.get("searchValue")), Integer.valueOf(allParams.get("searchValue")));
                    return new Output(HttpStatus.OK.value(), MovieManager.sortMovies(movies, sortedBy));
                }
                case "genre" -> {
                    String searchValue = allParams.getOrDefault("searchValue", "");
                    ArrayList <Movie> movies =MovieManager.getMoviesByGenre(searchValue);
                    return new Output(HttpStatus.OK.value(), MovieManager.sortMovies(movies, sortedBy));
                }
                default -> {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return new Output(HttpStatus.BAD_REQUEST.value(), "The server cannot or will not process the request due to something that is perceived to be a client error.");
                }
            }
        } catch (CommandException commandException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
        }
    }

    @GetMapping("/recommendedMovies")
    public Output getRecommendedMovies(HttpServletResponse response) throws CommandException {
        try {
            if (UserManager.loggedInUser == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new Output(HttpStatus.BAD_REQUEST.value(), "No logged in user found, please login first");
            }
            return new Output(HttpStatus.OK.value(), UserManager.getLoggedInUser().getWatchlistRecommendations());
        } catch (CommandException commandException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
        }

    }

    @PostMapping("/{id}/rate")
    @ResponseBody
    public Output rateMovie(HttpServletResponse response, @PathVariable Integer id, @RequestParam Map<String,String> allParams) throws CommandException {
        try {
            if (UserManager.loggedInUser == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new Output(HttpStatus.BAD_REQUEST.value(), "No logged in user found, please login first\"");
            }

            String userEmail = UserManager.getLoggedInUser().getEmail();
            Rating userRating = new Rating(userEmail, id, Integer.valueOf(allParams.get("rating")));
            return new Output(HttpStatus.OK.value(), MovieManager.addRating(userRating));
        } catch (CommandException commandException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
        }

    }

    @PostMapping("/{id}/comment")
    @ResponseBody
    public Output commentOnMovie(@RequestBody Map<String, String> body, HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        try {
            if (UserManager.loggedInUser == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new Output(HttpStatus.BAD_REQUEST.value(), "No logged in user found, please login first\"");
            }
            String userEmail = UserManager.getLoggedInUser().getEmail();
            Comment userComment = new Comment(userEmail, id, body.get("comment"));
            return new Output(HttpStatus.OK.value(), MovieManager.addComment(userComment));
        } catch (CommandException commandException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
        }
    }

    @GetMapping("/actors/{id}")
    public Output getActorMovies(HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        try {
            ActorManager.getActor(id);
            return new Output(HttpStatus.OK.value(), MovieManager.getActorMovies(id));
        } catch (CommandException commandException) {
            if (commandException.getErrorType().equals(ErrorType.ActorNotFound)) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new Output(HttpStatus.NOT_FOUND.value(), "The server can not find the movie.");
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
            }
        }
    }

    @GetMapping("/{id}/actors")
    public Output getMovieActors(HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        try {
            return new Output(HttpStatus.OK.value(), MovieManager.getMovieActors(id));
        } catch (CommandException commandException) {
            if (commandException.getErrorType().equals(ErrorType.MovieNotFound)) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new Output(HttpStatus.NOT_FOUND.value(), "The server can not find the movie.");
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
            }
        }
    }

    @GetMapping("/{id}/comments")
    public Output getMovieComments(HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        try {
            return new Output(HttpStatus.OK.value(), MovieManager.getMovieComments(id));
        } catch (CommandException commandException) {
            if (commandException.getErrorType().equals(ErrorType.MovieNotFound)) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new Output(HttpStatus.NOT_FOUND.value(), "The server can not find the movie.");
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return new Output(HttpStatus.INTERNAL_SERVER_ERROR.value(), commandException.getMessage());
            }
        }
    }
}
