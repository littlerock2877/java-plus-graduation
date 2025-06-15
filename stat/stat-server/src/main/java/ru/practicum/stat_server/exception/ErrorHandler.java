package ru.practicum.stat_server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingSe(final MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException with message {} was thrown", e.getMessage());
        Map map = new LinkedHashMap<String, String>();
        map.put("status", HttpStatus.BAD_REQUEST.name());
        map.put("reason", "Incorrectly made request.");
        map.put("message", e.getMessage());
        map.put("timestamp", LocalDateTime.now().format(formatter));
        return map;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleEventDateValidationException(final EventDateValidationException e) {
        log.error("EventDateValidationException with message {} was thrown", e.getMessage());
        Map map = new LinkedHashMap<String, String>();
        map.put("status", HttpStatus.BAD_REQUEST.name());
        map.put("reason", "Incorrectly made request.");
        map.put("message", e.getMessage());
        map.put("timestamp", LocalDateTime.now().format(formatter));
        return map;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final Exception e) {
        log.error("Exception with message {} was thrown", e.getMessage());
        Map map = new LinkedHashMap<String, String>();
        map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.name());
        map.put("reason", "Something get wrong.");
        map.put("message", e.getMessage());
        map.put("timestamp", LocalDateTime.now().format(formatter));
        return map;
    }
}