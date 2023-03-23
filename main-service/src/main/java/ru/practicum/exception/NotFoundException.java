package ru.practicum.exception;

public class NotFoundException extends RuntimeException {

    private final String reason;

    public NotFoundException(String msg, String reason) {
        super(msg);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
