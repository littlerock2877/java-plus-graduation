package ru.practicum.main_service.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.request.dto.RequestDto;
import ru.practicum.main_service.request.service.RequestService;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestPrivateController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Integer userId, @RequestParam(name = "eventId") Integer eventId) {
        log.info("Creating request by user with id {} for event with id {} - Started", userId, eventId);
        RequestDto requestDto = requestService.createRequest(userId, eventId);
        log.info("Creating request by user with id {} for event with id {} - Finished", userId, eventId);
        return requestDto;
    }

    @GetMapping
    public List<RequestDto> getCurrentUserRequests(@PathVariable Integer userId) {
        log.info("Getting requests by user with id {} - Started", userId);
        List<RequestDto> requests = requestService.getCurrentUserRequests(userId);
        log.info("Getting requests by user with id {} - Finished", userId);
        return requests;
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        log.info("Cancelling request with id {} by user with id {} - Started", requestId, userId);
        RequestDto requestDto = requestService.cancelRequests(userId, requestId);
        log.info("Cancelling request with id {} by user with id {} - Finished", requestId, userId);
        return requestDto;
    }
}