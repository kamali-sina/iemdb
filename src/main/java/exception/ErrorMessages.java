package exception;

import java.util.HashMap;

public class ErrorMessages {
    private static final String UserNotFound = "\"" + ErrorType.UserNotFound.name() + ": User does not exist.\"";
    private static final String MovieNotFound = "\"" + ErrorType.MovieNotFound.name() + ": Movie does not exist.\"";
    private static final String ActorNotFound = "\"" + ErrorType.ActorNotFound.name() + ": Actor does not exist.\"";
    private static final String CommentNotFound = "\""
            + ErrorType.CommentNotFound.name() + ": No comment with this id found.\"";
    private static final String MovieAlreadyExists = "\""
            + ErrorType.MovieAlreadyExists.name() + ": Movie is already added to watch list.\"";
    private static final String AgeLimitError = "\"" + ErrorType.AgeLimitError.name() +
            ": User's age does not meet requirements.\"";
    private static final String InvalidCommand = "\"" + ErrorType.InvalidCommand.name() +
            ": Command does not have valid structure.\"";
    private static final String InvalidRateScore = "\"" + ErrorType.InvalidRateScore.name() +
            ": Rating does not have valid value.\"";
    private static final String InvalidVoteValue = "\"" + ErrorType.InvalidVoteValue.name() +
            ": Vote does not have valid value.\"";
    private static final String UserAlreadyExists = "\"" + ErrorType.UserAlreadyExists.name() +
            ": User with this email address already exists.\"";

    public HashMap<String, String> messages = new HashMap<>();

    public ErrorMessages() {
        messages.put(ErrorType.UserNotFound.name(), ErrorMessages.UserNotFound);
        messages.put(ErrorType.MovieNotFound.name(), ErrorMessages.MovieNotFound);
        messages.put(ErrorType.ActorNotFound.name(), ErrorMessages.ActorNotFound);
        messages.put(ErrorType.CommentNotFound.name(), ErrorMessages.CommentNotFound);
        messages.put(ErrorType.MovieAlreadyExists.name(), ErrorMessages.MovieAlreadyExists);
        messages.put(ErrorType.AgeLimitError.name(), ErrorMessages.AgeLimitError);
        messages.put(ErrorType.InvalidCommand.name(), ErrorMessages.InvalidCommand);
        messages.put(ErrorType.InvalidRateScore.name(), ErrorMessages.InvalidRateScore);
        messages.put(ErrorType.InvalidVoteValue.name(), ErrorMessages.InvalidVoteValue);
        messages.put(ErrorType.UserAlreadyExists.name(), ErrorMessages.UserAlreadyExists);
    }
}
