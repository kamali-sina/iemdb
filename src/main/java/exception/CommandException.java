package exception;

public class CommandException extends Exception {
    private static final ErrorMessages errorMessages = new ErrorMessages();
    private ErrorType errorType;

    public CommandException(ErrorType errorType) {
        super(errorMessages.messages.get(errorType.name()));
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
