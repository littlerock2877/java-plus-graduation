package ru.practicum.main_service.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.service.CompilationService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Getting compilation list of size {} where pinned is {} from element {} - Started.",
                size, pinned, from);
        List<CompilationDto> compilations = service.getCompilations(pinned, from, size);
        log.info("Getting compilation list of size {} where pinned is {} from element {} - Finished.",
                size, pinned, from);
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Integer compId) {
        log.info("Getting compilation with id {} - Started.", compId);
        CompilationDto compilation = service.getById(compId);
        log.info("Getting compilation with id {} - Finished.", compId);
        return compilation;
    }
}
