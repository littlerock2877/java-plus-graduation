package ru.practicum.main_service.event.service;

import client.RestStatClient;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoryRepository;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.enums.StateActionForAdmin;
import ru.practicum.main_service.event.enums.StateActionForUser;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.Like;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.event.repository.LikeRepository;
import ru.practicum.main_service.event.repository.LocationRepository;
import ru.practicum.main_service.exception.EventDateValidationException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.main_service.user.mapper.UserMapper;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final EventMapper eventMapper;
    private final RestStatClient restStatClient;
    private static final String START = "1970-01-01 00:00:00";
    private static final String END = "3000-12-31 23:59:59";

    @Override
    public List<EventShortDto> getEventsByUser(Integer userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiatorId(userId, page).stream().map(event -> eventMapper.toEventShortDto(event)).toList();
    }

    @Override
    public EventFullDto createEvent(Integer userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate() != null && !newEventDto.getEventDate().isAfter(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            throw new EventDateValidationException("Event date should be in 2+ hours after now");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", newEventDto.getCategory())));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        Event event = eventMapper.toModelByNew(newEventDto, category, user);
        event.setLocation(locationRepository.save(newEventDto.getLocation()));
        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventFullInformation(Integer userId, Integer eventId) {
        return eventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId))));
    }

    @Override
    public EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserDto updateEventUserDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (event.getPublishedOn() != null) {
            throw new InvalidParameterException("Event is already published");
        }
        if (updateEventUserDto.getEventDate() != null && !updateEventUserDto.getEventDate().isAfter(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            throw new EventDateValidationException("Event date should be in 2+ hours after now");
        }
        if (updateEventUserDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserDto.getAnnotation());
        }
        if (updateEventUserDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventUserDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", updateEventUserDto.getCategory())));
            event.setCategory(category);
        }
        if (updateEventUserDto.getDescription() != null) {
            event.setDescription(updateEventUserDto.getDescription());
        }
        if (updateEventUserDto.getEventDate() != null) {
            event.setEventDate(updateEventUserDto.getEventDate());
        }
        if (updateEventUserDto.getLocation() != null) {
            event.setLocation(updateEventUserDto.getLocation());
        }
        if (updateEventUserDto.getPaid() != null) {
            event.setPaid(updateEventUserDto.getPaid());
        }
        if (updateEventUserDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserDto.getParticipantLimit());
        }
        if (updateEventUserDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserDto.getRequestModeration());
        }
        if (updateEventUserDto.getTitle() != null) {
            event.setTitle(updateEventUserDto.getTitle());
        }

        if (updateEventUserDto.getStateAction() != null) {
            if (updateEventUserDto.getStateAction().equals(StateActionForUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto adminUpdateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (updateEventAdminRequest.getStateAction() == StateActionForAdmin.PUBLISH_EVENT && event.getState() != EventState.PENDING) {
            throw new DataIntegrityViolationException("Event should be in PENDING state");
        }
        if (updateEventAdminRequest.getStateAction() == StateActionForAdmin.REJECT_EVENT && event.getState() == EventState.PUBLISHED) {
            throw new DataIntegrityViolationException("Event can be rejected only in PENDING state");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateActionForAdmin.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEventAdminRequest.getStateAction().equals(StateActionForAdmin.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", updateEventAdminRequest.getCategory())));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(locationRepository.save(updateEventAdminRequest.getLocation()));
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> adminGetAllEvents(AdminEventParams adminEventParams) {
        Pageable page = PageRequest.of(adminEventParams.getFrom(), adminEventParams.getSize());
        if (adminEventParams.getRangeStart() == null) {
            adminEventParams.setRangeStart(LocalDateTime.now());
        }
        if (adminEventParams.getRangeEnd() == null) {
            adminEventParams.setRangeEnd(LocalDateTime.now().plusYears(1));
        }
        List<EventFullDto> events = eventMapper.toEventFullDto(eventRepository.findAdminEvents(
                adminEventParams.getUsers(),
                adminEventParams.getStates(),
                adminEventParams.getCategories(),
                adminEventParams.getRangeStart(),
                adminEventParams.getRangeEnd(),
                page));
        if (events.isEmpty()) {
            return List.of();
        }
        return events;
    }

    @Override
    public List<EventFullDto> adminGetEventsLikedByUser(Integer userId) {
        List<Integer> eventIds = getEventIdsLikedByUser(userId);
        return eventMapper.toEventFullDto(eventRepository.findAllById(eventIds));
    }

    @Override
    public List<EventShortDto> getAllLikedEvents(Integer userId) {
        List<Integer> eventIds = getEventIdsLikedByUser(userId);
        return eventRepository.findAllById(eventIds).stream()
                .map(eventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public List<EventShortDto> publicGetAllEvents(EventRequestParam eventRequestParam) {
        Pageable page = PageRequest.of(eventRequestParam.getFrom() / eventRequestParam.getSize(), eventRequestParam.getSize());

        if (eventRequestParam.getRangeStart() == null || eventRequestParam.getRangeEnd() == null) {
            eventRequestParam.setRangeStart(LocalDateTime.now());
            eventRequestParam.setRangeEnd(eventRequestParam.getRangeStart().plusYears(1));
        }

        if (eventRequestParam.getRangeStart().isAfter(eventRequestParam.getRangeEnd())) {
            throw new EventDateValidationException("End date should be before start date");
        }

        List<Event> events = eventRepository.findPublicEvents(
                eventRequestParam.getText(),
                eventRequestParam.getCategory(),
                eventRequestParam.getPaid(),
                eventRequestParam.getRangeStart(),
                eventRequestParam.getRangeEnd(),
                eventRequestParam.getOnlyAvailable(),
                page);
        if (events.isEmpty()) {
            return List.of();
        }

        if (eventRequestParam.getSort() != null) {
            return switch (eventRequestParam.getSort()) {
                case EVENT_DATE -> events.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .map(eventMapper::toEventShortDto)
                        .toList();
                case VIEWS -> events.stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .map(eventMapper::toEventShortDto)
                        .toList();
            };
        }
        for (Event event : events) {
            addViews("/events/" + event.getId(), event);
        }
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }

    @Override
    public EventFullDto publicGetEvent(Integer eventId) {
        Event event = getEvent(eventId);
        addViews("/events/" + event.getId(), event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return eventFullDto;
    }

    @Override
    public Long addLike(Integer userId, Integer eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (!likeRepository.existsByUserIdAndEventId(userId, eventId)) {
            Like like = new Like(user, event);
            likeRepository.save(like);
        }
        return likeRepository.countByEventId(eventId);
    }

    @Override
    public Long removeLike(Integer userId, Integer eventId) {
        if (likeRepository.existsByUserIdAndEventId(userId, eventId)) {
            likeRepository.deleteByUserIdAndEventId(userId, eventId);
        }
        return likeRepository.countByEventId(eventId);
    }

    @Override
    public List<UserShortDto> getLikedUsers(Integer eventId) {
        Event event = getEvent(eventId);
        addViews("/events/" + event.getId(), event);
        List<Like> likes = likeRepository.findAllByEventId(eventId);
        return likes.stream().map(like -> userMapper.toUserShortDto(like.getUser())).toList();
    }

    private List<Integer> getEventIdsLikedByUser(Integer userId) {
        User user = getUser(userId);
        List<Like> likes = likeRepository.findAllByUserId(userId);
        if (likes.isEmpty()) {
            throw new NotFoundException(String.format("User with id=%d did not like any events", userId));
        }
        return likes.stream()
                .map(Like::getEvent)
                .map(Event::getId)
                .toList();
    }

    private void addViews(String uri, Event event) {
        ViewStatsDto[] views = restStatClient.getStats(START, END, List.of(uri), false).toArray(new ViewStatsDto[0]);
        if (views.length == 0) {
            event.setViews(0L);
        } else {
            event.setViews((long)views.length);
        }
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Event getEvent(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException(String.format("Event with id=%d was not published", eventId));
        }
        return event;
    }
}