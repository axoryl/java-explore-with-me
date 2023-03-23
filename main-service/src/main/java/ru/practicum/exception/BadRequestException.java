package ru.practicum.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg) {
        super(msg);
    }

    public String getReason() {
        return "Incorrectly made request.";
    }
}
