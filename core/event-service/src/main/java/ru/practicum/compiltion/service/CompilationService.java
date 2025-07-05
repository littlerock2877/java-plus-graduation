package ru.practicum.compiltion.service;

import ru.practicum.compiltion.dto.CompilationDto;
import ru.practicum.compiltion.dto.NewCompilationDto;
import ru.practicum.compiltion.dto.UpdateCompilationRequest;

import java.util.Collection;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(Long id);

    CompilationDto update(Long id, UpdateCompilationRequest updateCompilationRequest);

    Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}