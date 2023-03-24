package ru.practicum.exception;

public class ConflictException extends RuntimeException {

    private final static String REASON_MSG = "For the requested operation the conditions are not met.";

    public ConflictException(String msg) {
        super(msg);
    }

    public String getReason() {
        return REASON_MSG;
    }
}
