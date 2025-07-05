package ru.practicum.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@Component
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent(),
                request.getRequester(),
                request.getStatus().toString()
        );
    }

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getEvent(),
                request.getRequester(),
                request.getStatus(),
                request.getCreated()
        );
    }
}