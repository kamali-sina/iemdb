package com.example.iemdb;

import exception.CommandException;
import exception.ErrorType;
import main.Comment;
import main.Vote;
import manager.MovieManager;
import manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @RequestMapping("/{id}/vote")
    public ResponseEntity<?> voteComment(HttpServletRequest request, @PathVariable Integer id) {
        if (UserManager.loggedInUser == null) {
            return new ResponseEntity<>("no logged in user found, please login first", HttpStatus.BAD_REQUEST);
        }
        try {
            Integer vote = Integer.valueOf(request.getParameter("vote"));
            Comment comment = MovieManager.findComment(id);
            if (comment == null) {
                throw new CommandException(ErrorType.CommentNotFound);
            }
            comment.addVote(new Vote(UserManager.loggedInUser.getEmail(), id, vote));
        } catch (CommandException ce) {
            return new ResponseEntity<>(ce.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("commentId was incorrect", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("voted comment successfully", HttpStatus.OK);
    }
}