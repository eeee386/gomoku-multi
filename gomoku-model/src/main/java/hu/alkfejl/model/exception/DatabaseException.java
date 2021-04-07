package hu.alkfejl.model.exception;

public class DatabaseException extends Exception {
    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }
}
