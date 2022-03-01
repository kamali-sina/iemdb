package input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class GetMoviesByGenreInput {
    private String genre;

    @JsonCreator
    public GetMoviesByGenreInput(@JsonProperty("genre") String genre) throws CommandException {
        if (genre == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
