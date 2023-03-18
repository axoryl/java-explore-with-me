package ru.practicum.exception;

public class BadRequestException extends RuntimeException {

    private final String reason;

    public BadRequestException(String msg, String reason) {
        super(msg);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
