package ru.practicum.main_service.event.controller;

import client.RestStatClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventRequestParam;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.enums.EventSort;
import ru.practicum.main_service.event.service.EventService;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.main_service.utility.Constants;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {
    private final EventService eventService;
    private final RestStatClient restStatClient;

    @GetMapping
    public List<EventShortDto> publicGetEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) EventSort sort,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        EventRequestParam reqParam = new EventRequestParam(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("Getting public events - Started");
        saveHit(request);
        List<EventShortDto> events = eventService.publicGetAllEvents(reqParam);
        log.info("Getting public events - Finished");
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto publicGetEvent(@PathVariable("eventId") Integer eventId,
                                       HttpServletRequest request) {
        log.info("Getting public event with id {} - Started", eventId);
        saveHit(request);
        EventFullDto event = eventService.publicGetEvent(eventId);
        log.info("Getting public event with id {} - Finished", eventId);
        return event;
    }

    @GetMapping("/{eventId}/likes")
    public List<UserShortDto> publicGetLikedUsers(@PathVariable("eventId") Integer eventId, HttpServletRequest request) {
        log.info("Getting liked users for event with id {} - Started", eventId);
        saveHit(request);
        List<UserShortDto> likedUsers = eventService.getLikedUsers(eventId);
        log.info("Getting liked users for event with id {} - Finished", eventId);
        return likedUsers;
    }

    private void saveHit(HttpServletRequest request) {
        EndpointHitDto hit = new EndpointHitDto();
        hit.setApp("main-service");
        hit.setUri(request.getRequestURI());
        hit.setIp(request.getRemoteAddr());
        hit.setTimestamp(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        restStatClient.saveHit(hit);
    }
}
