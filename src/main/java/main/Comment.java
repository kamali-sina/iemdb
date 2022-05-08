package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;
import repository.ConnectionPool;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Comment {
    private static Integer count = 1;

    private String userEmail;
    private Integer movieId;
    private String text;
    private Integer commentId;
    private HashMap<String, Vote> votes = new HashMap<>();
    private Integer numberOfLikes = 0;
    private Integer numberOfDislikes = 0;
    private String nickname;

    @JsonCreator
    public Comment(@JsonProperty("userEmail") String userEmail,
                   @JsonProperty("movieId") Integer movieId,
                   @JsonProperty("text") String text) throws CommandException {
        if (userEmail == null ||
                movieId == null ||
                text == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.text = text;
    }

    public static Integer getCount() {
        return count++;
    }

    public static void setCount(Integer count) {
        Comment.count = count;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNickname() {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select nickName from Users inner join Comments on Users.email = Comments.userEmail AND Users.email = \""+this.userEmail+"\"");

            String nickName = "";

            if (result.next()) {
                nickName = result.getString("nickName");
            }
            result.close();
            stmt.close();
            con.close();

            return nickName;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String print() {
        String value = "";
        value += this.userEmail;
        value += " " + this.movieId.toString();
        value += " " + this.text;
        return value;
    }

    public Integer getId() {
        return this.commentId;
    }

    public void setId(Integer id) {
        this.commentId = id;
    }

    public Integer getNumberOfLikes() {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select Count(*) as numberOfLikes from Votes where commentId = \"" + this.getId() + "\" and vote = \"1\"");

            Integer numberOfLikes = 0;

            if (result.next()) {
                numberOfLikes = result.getInt("numberOfLikes");
            }
            result.close();
            stmt.close();
            con.close();

            return numberOfLikes;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Integer getNumberOfDislikes() {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select Count(*) as numberOfDislikes from Votes where commentId = \"" + this.getId() + "\" and vote = \"-1\"");

            Integer numberOfDislikes = 0;

            if (result.next()) {
                numberOfDislikes = result.getInt("numberOfDislikes");
            }
            result.close();
            stmt.close();
            con.close();

            return numberOfDislikes;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setNumberOfDislikes(Integer numberOfDislikes) {
        this.numberOfDislikes = numberOfDislikes;
    }

    public String addVote(Vote vote) throws CommandException {
        Integer voteValue = vote.getVote();
        if (!((-1 <= voteValue) && (voteValue <= 1))) {
            throw new CommandException(ErrorType.InvalidVoteValue);
        }
        if (votes.containsKey(vote.getUserEmail())) {
            this.updateVotes(votes.get(vote.getUserEmail()).getVote(), vote.getVote());
            this.votes.put(vote.getUserEmail(), vote);
            return "\"comment vote updated successfully.\"";
        }
        this.votes.put(vote.getUserEmail(), vote);
        this.updateVotes(0, vote.getVote());
        return "\"comment voted successfully.\"";
    }

    public void updateVotes(Integer previousValue, Integer newValue) {
        if (!previousValue.equals(newValue)) {
            if (previousValue == -1) {
                this.numberOfDislikes -= 1;
                this.numberOfLikes += newValue;
            } else if (previousValue == 0) {
                switch (newValue) {
                    case 1 -> numberOfLikes += 1;
                    case -1 -> numberOfDislikes += 1;
                }
            } else if (previousValue == 1) {
                numberOfLikes -= 1;
                numberOfDislikes -= newValue;
            }
        }
    }
}
