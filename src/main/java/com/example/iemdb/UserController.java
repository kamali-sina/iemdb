package com.example.iemdb;

import exception.CommandException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.User;
import manager.AuthenticationManager;
import manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import output.Output;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    @PostMapping("/login")
    public Output loginUser(@RequestBody Map<String, String> body, HttpServletResponse response) throws CommandException, NoSuchAlgorithmException {
        String email = body.get("email");
        String password = User.getHash(body.get("password"));
        try {
            User user = UserManager.getUser(email, password);

            String jwtToken = AuthenticationManager.generateJWT(user);

            return new Output(HttpStatus.OK.value(), jwtToken);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Output logoutUser(HttpServletRequest httpServletRequest, HttpServletResponse response) throws CommandException {
        User user = (User)httpServletRequest.getAttribute("user");

        return new Output(HttpStatus.OK.value(), "user logged out successfully");
    }

    @PostMapping("/signup")
    public Output signupUser(@RequestBody Map<String, String> body, HttpServletResponse response) throws CommandException {
        String name = body.get("name");
        String username = body.get("username");
        String email = body.get("email");
        String birthdate = body.get("birthdate");
        String password = body.get("password");
        try {
            User user = new User(email, password, username, name, birthdate);
            UserManager.addUser(user);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), "could not signup user with given info.");
        }
        return new Output(HttpStatus.OK.value(), "user signed up successfully");
    }

    @GetMapping("/watchlist")
    public Output getWatchlist(HttpServletResponse response, HttpServletRequest httpServletRequest) throws CommandException {
        User user = (User)httpServletRequest.getAttribute("user");
        return new Output(HttpStatus.OK.value(), user.getWatchList());
    }

    @PostMapping("/watchlist")
    public Output addToWatchlist(@RequestBody Map<String, String> body, HttpServletResponse response, HttpServletRequest httpServletRequest) throws CommandException {
        try {
            User user = (User)httpServletRequest.getAttribute("user");
            Integer movieId = Integer.valueOf(body.get("movieId"));
            user.addToWatchList(movieId);
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
    public Output removeFromWatchlist(HttpServletRequest httpServletRequest, @RequestBody Map<String, String> body, HttpServletResponse response) throws CommandException {
        User user = (User)httpServletRequest.getAttribute("user");

        try {
            Integer movieId = Integer.valueOf(body.get("movieId"));
            user.removeFromWatchList(movieId);
        } catch (CommandException ce) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), ce.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), "movieId was incorrect");
        }

        return new Output(HttpStatus.OK.value(), "movie removed from watchlist successfully");
    }
}