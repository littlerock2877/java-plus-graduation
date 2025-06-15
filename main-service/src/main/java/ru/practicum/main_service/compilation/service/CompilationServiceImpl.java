package ru.practicum.main_service.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.compilation.mapper.CompilationMapper;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.repository.CompilationRepository;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compRepo;
    private final EventRepository eventRepo;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compRepo.findCompilations(pinned, pageable).stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public CompilationDto getById(Integer id) {
        return mapper.map(compRepo
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id %s is not found", id))));
    }

    @Override
    public CompilationDto add(NewCompilationDto dto) {
        Compilation newComp = mapper.map(dto);
        if (dto.getEvents() != null) {
            newComp.setEvents(eventRepo.findAllById(dto.getEvents()));
        } else {
            newComp.setEvents(new ArrayList<>());
        }
        newComp = compRepo.save(newComp);
        return mapper.map(newComp);
    }

    @Override
    public CompilationDto update(Integer id, UpdateCompilationDto dto) {
        Compilation oldComp = compRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id %s is not found", id)));
        updater(oldComp, dto);
        return mapper.map(compRepo.save(oldComp));
    }

    @Override
    public void delete(Integer id) {
        if (compRepo.existsById(id)) {
            compRepo.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Compilation with id %s is not found", id));
        }
    }

    private void updater(Compilation oldComp, UpdateCompilationDto dto) {
        if (dto.getEvents() != null) {
            List<Event> newEvents = eventRepo.findAllById(dto.getEvents());
            oldComp.getEvents().clear();
            oldComp.getEvents().addAll(newEvents);
        }
        if (dto.isPinned() != oldComp.isPinned()) {
            oldComp.setPinned(dto.isPinned());
        }
        if (dto.getTitle() != null) {
            oldComp.setTitle(dto.getTitle());
        }
    }
}
