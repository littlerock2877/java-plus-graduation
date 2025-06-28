package ru.practicum.main_service.exception;

public class EventDateValidationException extends RuntimeException {
    public EventDateValidationException(String message) {
        super(message);
    }
}