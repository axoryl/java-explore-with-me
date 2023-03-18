package ru.practicum.exception;

public class ConflictException extends RuntimeException {

    private final String reason;

    public ConflictException(String msg, String reason) {
        super(msg);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
