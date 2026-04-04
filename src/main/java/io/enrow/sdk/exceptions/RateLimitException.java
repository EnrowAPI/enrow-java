package io.enrow.sdk.exceptions;

public class RateLimitException extends EnrowException {
    public RateLimitException(String message) {
        super(429, "RateLimitExceeded", message);
    }
}
