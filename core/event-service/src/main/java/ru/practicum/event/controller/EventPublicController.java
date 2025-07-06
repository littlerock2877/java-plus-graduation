package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.event.dto.RecommendationDto;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestParam;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.RecommendationService;
import ru.practicum.ewm.client.RestStatClient;
import ru.practicum.ewm.client.UserActionType;
import ru.practicum.utility.Constants;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {
    private final EventService eventService;
    private final RestStatClient restStatClient;
    private final RecommendationService recommendationService;

    @GetMapping("/recommendations")
    public List<RecommendationDto> getRecommendations(
            @RequestHeader("X-EWM-USER-ID") Integer userId, @RequestParam(name = "size", defaultValue = "10") int size) {
        return recommendationService.getRecommendations(userId, size);
    }

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
        List<EventShortDto> events = eventService.publicGetAllEvents(reqParam);
        log.info("Getting public events - Finished");
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto publicGetEvent(@PathVariable("eventId") Integer eventId,
                                       @RequestHeader("X-EWM-USER-ID") Integer userId) {
        log.info("Getting public event with id {} - Started", eventId);
        EventFullDto event = eventService.publicGetEvent(eventId);
        log.info("Getting public event with id {} - Finished", eventId);
        return event;
    }

    @GetMapping("/{eventId}/likes")
    public List<UserDto> publicGetLikedUsers(@PathVariable("eventId") Integer eventId) {
        log.info("Getting liked users for event with id {} - Started", eventId);
        List<UserDto> likedUsers = eventService.getLikedUsers(eventId);
        log.info("Getting liked users for event with id {} - Finished", eventId);
        return likedUsers;
    }

    @PutMapping("/{eventId}/like")
    public Long addLike(@PathVariable(name = "eventId") Integer eventId,
                        @RequestHeader("X-EWM-USER-ID") Integer userId) {
        log.info("Adding like to event with id {} from user with id {} - Started", eventId, userId);
        long likeCount = eventService.addLike(userId, eventId);
        log.info("Adding like to event with id {} from user with id {} - Finished", eventId, userId);
        return likeCount;
    }

    @DeleteMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.GONE)
    public Long removeLike(@PathVariable(name = "eventId") Integer eventId,
                           @RequestHeader("X-EWM-USER-ID") Integer userId) {
        log.info("Removing like from event with id {} from user with id {} - Started", eventId, userId);
        long likeCount = eventService.removeLike(userId, eventId);
        log.info("Removing like from event with id {} from user with id {} - Finished", eventId, userId);
        return likeCount;
    }
}
