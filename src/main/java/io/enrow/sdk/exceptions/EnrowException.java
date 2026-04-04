package io.enrow.sdk.exceptions;

public class EnrowException extends RuntimeException {
    public final int status;
    public final String error;

    public EnrowException(int status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }
}
