import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class GetMovieByIdInput {
    private Integer movieId;

    @JsonCreator
    public GetMovieByIdInput(@JsonProperty("movieId") Integer movieId) throws CommandException {
        if (movieId == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.movieId = movieId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
