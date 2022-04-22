package com.example.iemdb;

import exception.CommandException;
import exception.ErrorType;
import main.Comment;
import main.Vote;
import manager.MovieManager;
import manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import output.Output;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/comments")
public class CommentController {

    @RequestMapping("/{id}/vote")
    public Output voteComment(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        if (UserManager.loggedInUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new Output(HttpStatus.UNAUTHORIZED.value(), "no logged in user found, please login first.");
        }
        try {
            Integer vote = Integer.valueOf(request.getParameter("vote"));
            Comment comment = MovieManager.findComment(id);
            if (comment == null) {
                throw new CommandException(ErrorType.CommentNotFound);
            }
            comment.addVote(new Vote(UserManager.loggedInUser.getEmail(), id, vote));
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