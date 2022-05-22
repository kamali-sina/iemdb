package com.example.iemdb;

import exception.CommandException;
import main.User;
import org.springframework.http.HttpStatus;
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
    public Output voteComment(HttpServletRequest httpServletRequest, @RequestBody Map<String, String> body, HttpServletResponse response, @PathVariable Integer id) throws CommandException {
        User user = (User)httpServletRequest.getAttribute("user");

        try {
            Integer vote = Integer.valueOf(body.get("vote"));
            user.addVoteToComment(id, vote);
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