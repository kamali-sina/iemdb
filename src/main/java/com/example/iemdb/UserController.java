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

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            User user = UserManager.getUser(email);
            if (!user.getPassword().equals(password)) {
                throw new CommandException(ErrorType.UserNotFound);
            }
            UserManager.logInUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>("username and password combination is wrong", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("user logged in successfully", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() throws CommandException {
        if (UserManager.loggedInUser == null) {
            return new ResponseEntity<>("no logged in user found, please login first", HttpStatus.BAD_REQUEST);
        }
        UserManager.logOutUser();
        return new ResponseEntity<>("user logged out successfully", HttpStatus.OK);
    }

    @GetMapping("/watchlist")
    public ResponseEntity<?> getWatchlist() throws CommandException {
        if (UserManager.loggedInUser == null) {
            return new ResponseEntity<>("no logged in user found, please login first", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(UserManager.loggedInUser.getWatchList().values(), HttpStatus.OK);
    }

    @PostMapping("/watchlist")
    public ResponseEntity<?> addToWatchlist(HttpServletRequest request) throws CommandException {
        if (UserManager.loggedInUser == null) {
            return new ResponseEntity<>("no logged in user found, please login first", HttpStatus.BAD_REQUEST);
        }
        try {
            Integer movieId = Integer.valueOf(request.getParameter("movieId"));
            Movie movie = MovieManager.getMovie(movieId);
            UserManager.loggedInUser.addToWatchList(movie);
        } catch (CommandException ce) {
            return new ResponseEntity<>(ce.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("movieId was incorrect", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("movie added to watchlist successfully", HttpStatus.OK);
    }

    @DeleteMapping("/watchlist")
    public ResponseEntity<?> removeFromWatchlist(HttpServletRequest request) throws CommandException {
        if (UserManager.loggedInUser == null) {
            return new ResponseEntity<>("no logged in user found, please login first", HttpStatus.BAD_REQUEST);
        }
        try {
            Integer movieId = Integer.valueOf(request.getParameter("movieId"));
            Movie movie = MovieManager.getMovie(movieId);
            UserManager.loggedInUser.removeFromWatchList(movie);
        } catch (CommandException ce) {
            return new ResponseEntity<>(ce.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("movieId was incorrect", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("movie removed from watchlist successfully", HttpStatus.OK);
    }

}