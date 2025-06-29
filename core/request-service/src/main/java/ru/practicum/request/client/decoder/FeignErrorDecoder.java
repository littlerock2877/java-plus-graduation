package ru.practicum.request.client.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import ru.practicum.request.exception.DataValidationException;
import ru.practicum.request.exception.InternalServerErrorException;
import ru.practicum.request.exception.NotFoundException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new DataValidationException("Bad argument requests for method: %s".formatted(methodKey));
            case 404 -> new NotFoundException("Resource not found for method: %s".formatted(methodKey));
            case 500 -> new InternalServerErrorException("Server error occurred in method: %s".formatted(methodKey));
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}