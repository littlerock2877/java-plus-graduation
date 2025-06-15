package ru.practicum.main_service.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper mapper;

    public Compilation map(NewCompilationDto dto) {
        return Compilation.builder()
                .pinned(dto.isPinned())
                .title(dto.getTitle())
                .build();
    }

    public Compilation map(UpdateCompilationDto dto) {
        return Compilation.builder()
                .pinned(dto.isPinned())
                .title(dto.getTitle())
                .build();
    }

    public CompilationDto map(Compilation comp) {
        return CompilationDto.builder()
                .id(comp.getId())
                .pinned(comp.isPinned())
                .title(comp.getTitle())
                .events(mapEvents(comp.getEvents()))
                .build();
    }

    private Collection<EventShortDto> mapEvents(Collection<Event> events) {
        return events.stream()
                .map(mapper::toEventShortDto)
                .toList();
    }
}
