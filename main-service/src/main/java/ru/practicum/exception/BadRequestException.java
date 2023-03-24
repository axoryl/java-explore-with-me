package ru.practicum.exception;

public class BadRequestException extends RuntimeException {

    private final static String REASON_MSG = "Incorrectly made request.";

    public BadRequestException(String msg) {
        super(msg);
    }

    public String getReason() {
        return REASON_MSG;
    }
}
