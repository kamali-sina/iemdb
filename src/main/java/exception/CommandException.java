package exception;

public class CommandException extends Exception {
    private static final ErrorMessages errorMessages = new ErrorMessages();

    public CommandException(ErrorType errorType) {
        super(errorMessages.messages.get(errorType.name()));
    }
}
