package io.enrow.sdk.exceptions;

public class InsufficientBalanceException extends EnrowException {
    public InsufficientBalanceException(String message) {
        super(422, "InsufficientBalance", message);
    }
}
