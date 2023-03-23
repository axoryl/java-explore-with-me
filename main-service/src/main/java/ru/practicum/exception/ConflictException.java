package ru.practicum.exception;

public class ConflictException extends RuntimeException {

    private String reason = "For the requested operation the conditions are not met.";

    public ConflictException(String msg) {
        super(msg);
    }

    public ConflictException(String msg, String reason) {
        super(msg);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
