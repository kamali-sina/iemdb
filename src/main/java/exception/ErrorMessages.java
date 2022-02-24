package exception;

import java.util.HashMap;

public class ErrorMessages {
    private static final String UserNotFound = "User does not exist.";
    private static final String MovieNotFound = "Movie does not exist.";
    private static final String ActorNotFound = "Actor does not exist.";
    private static final String CommentNotFound = "No comment with this id found.";
    private static final String MovieAlreadyExists = "Movie is already added to watch list.";
    private static final String AgeLimitError = "User's age does not meet requirements.";
    private static final String InvalidCommand = "Command does not have valid structure.";
    private static final String InvalidRateScore = "Rating does not have valid value";
    private static final String InvalidVoteValue = "Vote does not have valid value.";
    private static final String UserAlreadyExists = "User with this email address already exists.";

    public HashMap<String, String> messages = new HashMap<>();

    public ErrorMessages() {
        messages.put("UserNotFound", ErrorMessages.UserNotFound);
        messages.put("MovieNotFound", ErrorMessages.MovieNotFound);
        messages.put("ActorNotFound", ErrorMessages.ActorNotFound);
        messages.put("CommentNotFound", ErrorMessages.CommentNotFound);
        messages.put("MovieAlreadyExists", ErrorMessages.MovieAlreadyExists);
        messages.put("AgeLimitError", ErrorMessages.AgeLimitError);
        messages.put("InvalidCommand", ErrorMessages.InvalidCommand);
        messages.put("InvalidRateScore", ErrorMessages.InvalidRateScore);
        messages.put("InvalidVoteValue", ErrorMessages.InvalidVoteValue);
        messages.put("UserAlreadyExists", ErrorMessages.UserAlreadyExists);
    }
}
