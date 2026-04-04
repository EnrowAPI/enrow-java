package io.enrow.sdk.exceptions;

public class AuthenticationException extends EnrowException {
    public AuthenticationException(String message) {
        super(401, "Unauthorized", message);
    }
}
