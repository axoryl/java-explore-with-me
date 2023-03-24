package ru.practicum.exception;

public class NotFoundException extends RuntimeException {

    private final static String REASON_MSG = "The required object was not found.";

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String stringTemplate, Long id) {
        super(String.format(stringTemplate, id));
    }

    public String getReason() {
        return REASON_MSG;
    }
}
