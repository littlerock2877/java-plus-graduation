package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.UpdateEventUserDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Integer userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Creating event by user with id {} - Started", userId);
        EventFullDto createdDto = eventService.createEvent(userId, newEventDto);
        log.info("Creating event by user with id {} - Finished", userId);
        return createdDto;
    }

    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable Integer userId,
                                               @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting events for user with id {} - Started", userId);
        List<EventShortDto> events = eventService.getEventsByUser(userId, from, size);
        log.info("Getting events for user with id {} - Finished", userId);
        return events;
    }

    @GetMapping("/like")
    public List<EventShortDto> getAllLikedEvents(@PathVariable Integer userId) {
        log.info("Getting all liked events for user with id {} - Started", userId);
        List<EventShortDto> events = eventService.getAllLikedEvents(userId);
        log.info("Getting all liked events for user with id {} - Finished", userId);
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventFullInformation(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("Getting event with id {} by user with id {} - Started", eventId, userId);
        EventFullDto event = eventService.getEventFullInformation(userId, eventId);
        log.info("Getting event with id {} by user with id {} - Finished", eventId, userId);
        return event;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Integer userId, @PathVariable Integer eventId,
                                          @Valid @RequestBody UpdateEventUserDto updateEventUserDto) {
        log.info("Updating event with id {} by user with id {} - Started", eventId, userId);
        EventFullDto createdDto = eventService.updateEvent(userId, eventId, updateEventUserDto);
        log.info("Updating event with id {} by user with id {} - Finished", eventId, userId);
        return createdDto;
    }

    @PostMapping("/{eventId}/like")
    public Long addLike(@PathVariable(name = "eventId") Integer eventId,
                        @PathVariable(name = "userId") Integer userId) {
        log.info("Adding like to event with id {} from user with id {} - Started", eventId, userId);
        long likeCount = eventService.addLike(userId, eventId);
        log.info("Adding like to event with id {} from user with id {} - Finished", eventId, userId);
        return likeCount;
    }

    @DeleteMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.GONE)
    public Long removeLike(@PathVariable(name = "eventId") Integer eventId,
                           @PathVariable(name = "userId") Integer userId) {
        log.info("Removing like from event with id {} from user with id {} - Started", eventId, userId);
        long likeCount = eventService.removeLike(userId, eventId);
        log.info("Removing like from event with id {} from user with id {} - Finished", eventId, userId);
        return likeCount;
    }
}