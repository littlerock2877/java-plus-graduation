package ru.practicum.main_service.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.user.dto.UserShortDto;

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

    List<UserShortDto> getLikedUsers(Integer eventId);

    List<EventFullDto> adminGetEventsLikedByUser(Integer userId);

    List<EventShortDto> getAllLikedEvents(Integer userId);
}