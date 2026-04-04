package io.enrow.sdk.exceptions;

public class InsufficientBalanceException extends EnrowException {
    public InsufficientBalanceException(String message) {
        super(402, "InsufficientBalance", message);
    }
}
