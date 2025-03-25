package app.exception;

public class NoRefundsLeft extends RuntimeException {
    public NoRefundsLeft(String message) {
        super(message);
    }
}
