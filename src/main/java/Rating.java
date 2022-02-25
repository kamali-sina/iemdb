import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class Rating {
    public static final Integer minScore = 1;
    public static final Integer maxScore = 10;

    private String userEmail;
    private Integer movieId;
    private Integer score;

    @JsonCreator
    public Rating(@JsonProperty("userEmail") String userEmail,
                  @JsonProperty("movieId") Integer movieId,
                  @JsonProperty("score") Integer score) throws CommandException {
        if (userEmail == null ||
                movieId == null ||
                score == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.score = score;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}