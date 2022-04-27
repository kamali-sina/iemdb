package com.example.iemdb;

import exception.CommandException;
import exception.ErrorType;
import main.Movie;
import main.User;
import manager.MovieManager;
import manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import output.Output;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    @PostMapping("/login")
    public Output loginUser(@RequestBody Map<String, String> body, HttpServletResponse response) throws CommandException {
        String email = body.get("email");
        String password = body.get("password");
        try {
            User user = UserManager.getUser(email);
            if (!user.getPassword().equals(password)) {
                throw new CommandException(ErrorType.UserNotFound);
            }
            UserManager.logInUser(user);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), "username and password combination is wrong.");
        }
        return new Output(HttpStatus.OK.value(), "user logged in successfully");
    }

    @PostMapping("/logout")
    public Output logoutUser(HttpServletResponse response) throws CommandException {
        if (UserManager.loggedInUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new Output(HttpStatus.UNAUTHORIZED.value(), "no logged in user found, please login first.");
        }
        UserManager.logOutUser();
        return new Output(HttpStatus.OK.value(), "user logged out successfully");
    }

    @GetMapping("/watchlist")
    public Output getWatchlist(HttpServletResponse response) throws CommandException {
        if (UserManager.loggedInUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new Output(HttpStatus.UNAUTHORIZED.value(), "no logged in user found, please login first.");
        }
        return new Output(HttpStatus.OK.value(), UserManager.loggedInUser.getWatchList().values());
    }

    @PostMapping("/watchlist")
    public Output addToWatchlist(@RequestBody Map<String, String> body, HttpServletResponse response) throws CommandException {
        if (UserManager.loggedInUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new Output(HttpStatus.UNAUTHORIZED.value(), "no logged in user found, please login first.");
        }
        try {
            Integer movieId = Integer.valueOf(body.get("movieId"));
            Movie movie = MovieManager.getMovie(movieId);
            UserManager.loggedInUser.addToWatchList(movie);
        } catch (CommandException ce) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), ce.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), "movieId was incorrect");
        }
        return new Output(HttpStatus.OK.value(), "movie added to watchlist successfully");
    }

    @DeleteMapping("/watchlist")
    public Output removeFromWatchlist(@RequestBody Map<String, String> body, HttpServletResponse response) throws CommandException {
        if (UserManager.loggedInUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new Output(HttpStatus.UNAUTHORIZED.value(), "no logged in user found, please login first.");
        }
        try {
            Integer movieId = Integer.valueOf(body.get("movieId"));
            Movie movie = MovieManager.getMovie(movieId);
            UserManager.loggedInUser.removeFromWatchList(movie);
        } catch (CommandException ce) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), ce.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), "movieId was incorrect");
        }

        return new Output(HttpStatus.OK.value(), "movie removed from watchlist successfully");
    }

    @GetMapping("/loggedInUser")
    public Output getLoggedInUser(HttpServletResponse response) throws CommandException {
        return new Output(HttpStatus.OK.value(), UserManager.loggedInUser);
    }
}