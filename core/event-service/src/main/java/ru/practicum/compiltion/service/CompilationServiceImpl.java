package ru.practicum.compiltion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.compiltion.dto.CompilationDto;
import ru.practicum.compiltion.dto.NewCompilationDto;
import ru.practicum.compiltion.dto.UpdateCompilationRequest;
import ru.practicum.compiltion.mapper.CompilationMapper;
import ru.practicum.compiltion.model.Compilation;
import ru.practicum.compiltion.repository.CompilationRepository;
import ru.practicum.dto.UserDto;
import ru.practicum.event.client.UserClient;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserClient userClient;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation newCompilation = new Compilation();
        newCompilation.setTitle(newCompilationDto.getTitle());
        newCompilation.setPinned(newCompilationDto.getPinned());

        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIsIn(newCompilationDto.getEvents());
            newCompilation.setEvents(events);
        }
        if (newCompilation.getEvents() == null) {
            return compilationMapper.toCompilationDto(compilationRepository.save(newCompilation), new ArrayList<>());
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(newCompilation), getEvents(newCompilation.getEvents().stream().map(Event::getId).toList()));
    }

    @Override
    public void delete(Long id) {
        compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d not found".formatted(id)));

        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto update(Long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d not found".formatted(id)));
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIsIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        return compilationMapper.toCompilationDto(
                compilationRepository.save(compilation),
                getEvents(compilation.getEvents().stream().map(Event::getId).toList()));
    }

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);

        return compilations.stream()
                .map(compilation -> compilationMapper.toCompilationDto(
                        compilation,
                        getEvents(compilation.getEvents().stream().map(Event::getId).toList())))
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d not found".formatted(compId)));

        return compilationMapper.toCompilationDto(compilation, getEvents(compilation.getEvents().stream().map(Event::getId).toList()));
    }

    private List<EventShortDto> getEvents(List<Integer> eventsIds) {
        List<Event> events = eventRepository.findAllByIdIsIn(eventsIds);
        Map<Integer, CategoryDto> categoryDtoMap = getCategories(events.stream().map(Event::getCategory).map(Category::getId).toList());
        Map<Integer, UserDto> userDtoMap = getUsers(events.stream().map(Event::getInitiatorId).toList());
        return events.stream()
                .map(eventMapper::toEventShortDto)
                .toList();
    }

    private Map<Integer, CategoryDto> getCategories(List<Integer> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        return categories.stream()
                .map(categoryMapper::toCategoryDto)
                .toList().stream().collect(Collectors.toMap(CategoryDto::getId, category -> category));
    }

    private Map<Integer, UserDto> getUsers(List<Integer> userIds) {
        return userClient.getUsers(userIds, 0, 1000).stream()
                .collect(Collectors.toMap(UserDto::getId, user -> user));
    }
}