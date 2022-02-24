import exception.CommandException;
import exception.ErrorType;

import java.time.LocalDate;
import java.util.HashMap;

public class Comment {
    private static Integer count = 1;

    private String userEmail;
    private Integer movieId;
    private String text;
    private Integer id;
    private LocalDate date;
    private HashMap<String, Vote> votes;
    private Integer numberOfLikes = 0;
    private Integer numberOfDislikes = 0;

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

    public String print() {
        String value = "";
        value += this.userEmail;
        value += " " + this.movieId.toString();
        value += " " + this.text;
        return value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Integer getNumberOfDislikes() {
        return numberOfDislikes;
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
        }
        this.votes.put(vote.getUserEmail(), vote);
        this.updateVotes(0, vote.getVote());
        return "comment voted successfully";
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
