package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class Vote {
    private String userEmail;
    private Integer commentId;
    private Integer vote;

    @JsonCreator
    public Vote(@JsonProperty("userEmail") String userEmail,
                @JsonProperty("commentId") Integer commentId,
                @JsonProperty("vote") Integer vote) throws CommandException {
        if (userEmail == null ||
                commentId == null ||
                vote == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.userEmail = userEmail;
        this.commentId = commentId;
        this.vote = vote;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public void printData() {
        System.out.println(this.userEmail);
        System.out.println(this.commentId);
        System.out.println(this.vote);
    }
}
