package ru.practicum.stat_server.exception;

public class EventDateValidationException extends RuntimeException {
    public EventDateValidationException(String message) {
        super(message);
    }
}