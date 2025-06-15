package ru.practicum.main_service.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.request.dto.RequestDto;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.request.mapper.RequestMapper;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.repository.RequestRepository;
import ru.practicum.main_service.user.repository.UserRepository;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<RequestDto> getRequestsByOwnerOfEvent(Integer userId, Integer eventId) {
        return requestRepository.findAllByEventWithInitiator(userId, eventId).stream().map(request -> requestMapper.toRequestDto(request)).toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }

        List<Request> requests = requestRepository.findAllByEventWithInitiator(userId, eventId);
        List<Request> requestsToUpdate = requests.stream().filter(request -> requestStatusUpdateRequest.getRequestIds().contains(request.getId())).toList();

        if (requestsToUpdate.stream().anyMatch(request -> request.getStatus().equals(RequestStatus.CONFIRMED) && requestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED))) {
            throw new InvalidParameterException("request already confirmed");
        }

        if (event.getConfirmedRequests() + requestsToUpdate.size() > event.getParticipantLimit() && requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new InvalidParameterException("exceeding the limit of participants");
        }

        for (Request x : requestsToUpdate) {
            x.setStatus(RequestStatus.valueOf(requestStatusUpdateRequest.getStatus().toString()));
        }
        requestRepository.saveAll(requestsToUpdate);
        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
           event.setConfirmedRequests(event.getConfirmedRequests() + requestsToUpdate.size());
        }
        eventRepository.save(event);
        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            result.setConfirmedRequests(requestsToUpdate.stream().map(request -> requestMapper.toParticipationRequestDto(request)).toList());
        }

        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            result.setRejectedRequests(requestsToUpdate.stream().map(request -> requestMapper.toParticipationRequestDto(request)).toList());
        }
        return result;
    }

    @Override
    public RequestDto createRequest(Integer userId, Integer eventId) {
        if (requestRepository.existsByRequesterAndEvent(userId, eventId)) {
            throw new InvalidParameterException("Request already exists");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (event.getInitiator().getId().equals(userId)) {
            throw new InvalidParameterException("Can't create request by initiator");
        }

        if (event.getPublishedOn() == null) {
            throw new InvalidParameterException("Event is not published");
        }
        List<Request> requests = requestRepository.findAllByEvent(eventId);

        if (!event.getRequestModeration() && requests.size() >= event.getParticipantLimit()) {
            throw new InvalidParameterException("Member limit exceeded ");
        }

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(eventId);
        request.setRequester(userId);
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getCurrentUserRequests(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        return requestRepository.findAllByRequester(userId).stream().map(request -> requestMapper.toRequestDto(request)).toList();
    }

    @Override
    public RequestDto cancelRequests(Integer userId, Integer requestId) {
        Request request = requestRepository.findByRequesterAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }
}