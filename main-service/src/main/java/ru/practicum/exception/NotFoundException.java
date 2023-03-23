package ru.practicum.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String stringTemplate, Long id) {
        super(String.format(stringTemplate, id));
    }

    public String getReason() {
        return "The required object was not found.";
    }
}
