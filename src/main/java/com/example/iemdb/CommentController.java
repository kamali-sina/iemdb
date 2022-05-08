package com.example.iemdb;

import exception.CommandException;
import exception.ErrorType;
import main.Comment;
import main.Vote;
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
@RequestMapping("/comments")
public class CommentController {

    @RequestMapping("/{id}/vote")
    public Output voteComment(@RequestBody Map<String, String> body, HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        if (UserManager.loggedInUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new Output(HttpStatus.UNAUTHORIZED.value(), "no logged in user found, please login first.");
        }
        try {
            Integer vote = Integer.valueOf(body.get("vote"));
            Comment comment = MovieManager.findComment(id);
            if (comment == null) {
                throw new CommandException(ErrorType.CommentNotFound);
            }
            UserManager.loggedInUser.addVoteToComment(id, vote);
        } catch (CommandException ce) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), ce.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Output(HttpStatus.BAD_REQUEST.value(), "commentId was incorrect");
        }
        return new Output(HttpStatus.OK.value(), "voted comment successfully");

    }
}