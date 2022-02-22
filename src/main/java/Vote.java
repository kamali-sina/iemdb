import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Vote {
    private final String userEmail;
    private final String commentId;
    private final int vote;

    @JsonCreator
    public Vote(
            @JsonProperty("userEmail") String userEmail,
            @JsonProperty("commentId") String commentId,
            @JsonProperty("vote") int vote) {
        this.userEmail = userEmail;
        this.commentId = commentId;
        this.vote = vote;
    }

    public void printData() {
        System.out.println(this.userEmail);
        System.out.println(this.commentId);
        System.out.println(this.vote);
    }
}
