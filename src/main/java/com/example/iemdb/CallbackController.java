package com.example.iemdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.CommandException;
import exception.ErrorType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.Movie;
import main.User;
import manager.AuthenticationManager;
import manager.MovieManager;
import manager.UserManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import output.Output;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/callback")
public class CallbackController {
    @PostMapping("/")
    public Output addToWatchlist(@RequestBody Map<String, String> body, HttpServletResponse response, HttpServletRequest httpServletRequest) throws CommandException, IOException, SQLException {
        try {
            String code = body.get("code");
            System.out.println("code is:" + code);
            Document doc = Jsoup.connect("https://github.com/login/oauth/access_token")
                    .header("Accept", "application/json")
                    .data("client_id", "919a25257e88693e77ab")
                    .data("client_secret", "54d3dea9c16cc7d1b99fff97c45b6bf6d294bf3d")
                    .data("code", code).ignoreContentType(true).post();
            System.out.println(doc.wholeText());
            String token_field = "\"access_token\":\"";
            int starting_index = doc.wholeText().indexOf(token_field) + token_field.length();
            int ending_index = doc.wholeText().indexOf("\"", starting_index + 1);
            String access_token = doc.wholeText().substring(starting_index, ending_index);
            doc = Jsoup.connect("https://api.github.com/user")
                    .header("Authorization", "token " + access_token)
                    .ignoreContentType(true).get();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(doc.wholeText(), Map.class);
            String username = map.get("login").toString();
            String name = map.get("name").toString();
            String email = map.get("email").toString();
            String created_at = map.get("created_at").toString();
            System.out.println(username);
            System.out.println(name);
            System.out.println(email);
            System.out.println(created_at.substring(0, created_at.indexOf("T")));
            String birthDate = LocalDate.parse
                    (created_at.substring(0, created_at.indexOf("T"))).minusYears(18).toString();
            User user = new User(email, "null", username, name, birthDate);
            UserManager.addGitHubUser(user);
            String token = AuthenticationManager.generateJWT(user);

            return new Output(HttpStatus.OK.value(), token);
        } catch (Exception exception) {
            System.out.println("come on?");
            System.out.println(exception.getMessage());
            return new Output(HttpStatus.UNAUTHORIZED.value(), "Could not login with github");
        }
    }
}