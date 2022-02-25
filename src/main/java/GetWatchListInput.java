import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class GetWatchListInput {
    private String userEmail;

    @JsonCreator
    public GetWatchListInput(@JsonProperty("userEmail") String userEmail) throws CommandException {
        if (userEmail == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
