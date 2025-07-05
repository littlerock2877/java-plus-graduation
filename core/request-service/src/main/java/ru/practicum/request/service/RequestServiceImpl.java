package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.request.client.EventClient;
import ru.practicum.request.client.UserClient;
import ru.practicum.request.dto.EventFullDto;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.EventShortDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.exception.NotFoundException;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventClient eventClient;
    private final UserClient userClient;
    private final RequestMapper requestMapper;

    @Override
    public List<RequestDto> getRequestsByOwnerOfEvent(Integer userId, Integer eventId) {
        List<EventShortDto> events = eventClient.getEventsByUser(userId, 0, 1000);
        return requestRepository.findAllByEventIn(events.stream()
                        .map(EventShortDto::getId)
                        .filter(id -> Objects.equals(id, eventId))
                .toList()).stream()
                .map(requestMapper::toRequestDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        EventFullDto event = eventClient.findById(eventId);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }
        List<EventShortDto> events = eventClient.getEventsByUser(userId, 0, 1000);
        List<Request> requests = requestRepository.findAllByEventIn(events.stream()
                .map(EventShortDto::getId)
                .filter(id -> Objects.equals(id, eventId))
                .toList());
        List<Request> requestsToUpdate = requests.stream().filter(request -> requestStatusUpdateRequest.getRequestIds().contains(request.getId())).toList();

        if (requestsToUpdate.stream().anyMatch(request -> request.getStatus().equals(RequestStatus.CONFIRMED) && requestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED))) {
            throw new InvalidParameterException("request already confirmed");
        }

        if (getConfirmedRequestsCount(event.getId()) + requestsToUpdate.size() > event.getParticipantLimit() && requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new InvalidParameterException("exceeding the limit of participants");
        }

        for (Request x : requestsToUpdate) {
            x.setStatus(RequestStatus.valueOf(requestStatusUpdateRequest.getStatus().toString()));
        }
        requestRepository.saveAll(requestsToUpdate);
        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
           event.setConfirmedRequests(event.getConfirmedRequests() + requestsToUpdate.size());
        }
        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            result.setConfirmedRequests(requestsToUpdate.stream().map(requestMapper::toParticipationRequestDto).toList());
        }

        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            result.setRejectedRequests(requestsToUpdate.stream().map(requestMapper::toParticipationRequestDto).toList());
        }
        return result;
    }

    @Override
    public RequestDto createRequest(Integer userId, Integer eventId) {
        if (requestRepository.existsByRequesterAndEvent(userId, eventId)) {
            throw new InvalidParameterException("Request already exists");
        }
        EventFullDto event = eventClient.findById(eventId);
        if (event.getInitiator().equals(userId)) {
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
        userClient.findById(userId);
        return requestRepository.findAllByRequester(userId).stream().map(request -> requestMapper.toRequestDto(request)).toList();
    }

    @Override
    public RequestDto cancelRequests(Integer userId, Integer requestId) {
        Request request = requestRepository.findByRequesterAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public Long getConfirmedRequestsCount(Integer eventId) {
        return requestRepository.countConfirmedRequests(eventId);
    }
}