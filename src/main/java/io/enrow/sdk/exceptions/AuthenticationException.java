package io.enrow.sdk.exceptions;

public class AuthenticationException extends EnrowException {
    public AuthenticationException(String message) {
        this(401, message);
    }

    public AuthenticationException(int status, String message) {
        super(status, "Unauthorized", message);
    }
}
