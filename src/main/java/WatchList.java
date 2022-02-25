import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class WatchList {
    private Integer movieId;
    private String userEmail;

    @JsonCreator
    public WatchList(@JsonProperty("movieId") Integer movieId,
                     @JsonProperty("userEmail") String userEmail) throws CommandException {
        if (movieId == null ||
                userEmail == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.movieId = movieId;
        this.userEmail = userEmail;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
