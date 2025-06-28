package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class RequestPrivateController {
    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Integer userId, @RequestParam(name = "eventId") Integer eventId) {
        log.info("Creating request by user with id {} for event with id {} - Started", userId, eventId);
        RequestDto requestDto = requestService.createRequest(userId, eventId);
        log.info("Creating request by user with id {} for event with id {} - Finished", userId, eventId);
        return requestDto;
    }

    @GetMapping("/requests")
    public List<RequestDto> getCurrentUserRequests(@PathVariable Integer userId) {
        log.info("Getting requests by user with id {} - Started", userId);
        List<RequestDto> requests = requestService.getCurrentUserRequests(userId);
        log.info("Getting requests by user with id {} - Finished", userId);
        return requests;
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        log.info("Cancelling request with id {} by user with id {} - Started", requestId, userId);
        RequestDto requestDto = requestService.cancelRequests(userId, requestId);
        log.info("Cancelling request with id {} by user with id {} - Finished", requestId, userId);
        return requestDto;
    }

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDto> getRequestsByOwnerOfEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("Getting requests for event with id {} by user with id {} - Started", eventId, userId);
        List<RequestDto> requests = requestService.getRequestsByOwnerOfEvent(userId, eventId);
        log.info("Getting requests for event with id {} by user with id {} - Finished", eventId, userId);
        return requests;
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable Integer userId, @PathVariable Integer eventId, @RequestBody EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        log.info("Updating requests for event with id {} by user with id {} - Started", eventId, userId);
        EventRequestStatusUpdateResult updated = requestService.updateRequests(userId, eventId, requestStatusUpdateRequest);
        log.info("Updating requests for event with id {} by user with id {} - Finished", eventId, userId);
        return updated;
    }
}