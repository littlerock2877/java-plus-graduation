package ru.practicum.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.event.dto.AdminEventParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestParam;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserDto;

import java.util.List;

@Transactional(readOnly = true)
public interface EventService {
    List<EventShortDto> getEventsByUser(Integer userId, Integer from, Integer size);

    @Transactional
    EventFullDto createEvent(Integer userId, NewEventDto newEventDto);

    EventFullDto getEventFullInformation(Integer userId, Integer eventId);

    @Transactional
    EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserDto updateEventUserDto);

    @Transactional
    EventFullDto adminUpdateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> adminGetAllEvents(AdminEventParams adminEventParams);

    List<EventShortDto> publicGetAllEvents(EventRequestParam eventRequestParam);

    EventFullDto publicGetEvent(Integer eventId);

    @Transactional
    Long addLike(Integer userId, Integer eventId);

    @Transactional
    Long removeLike(Integer userId, Integer eventId);

    List<UserDto> getLikedUsers(Integer eventId);

    List<EventFullDto> adminGetEventsLikedByUser(Integer userId);

    List<EventShortDto> getAllLikedEvents(Integer userId);

    EventFullDto adminGetEventById(Integer eventId);
}