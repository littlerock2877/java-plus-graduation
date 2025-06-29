package ru.practicum.compiltion.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.compiltion.dto.CompilationDto;
import ru.practicum.compiltion.model.Compilation;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Component
public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(events)
                .pinned(compilation.getPinned())
                .build();
    }
}