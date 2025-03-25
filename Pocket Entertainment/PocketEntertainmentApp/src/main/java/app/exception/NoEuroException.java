package app.exception;

public class NoEuroException extends RuntimeException {
    public NoEuroException(String message) {
        super(message);
    }
}
