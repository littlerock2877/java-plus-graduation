package ru.practicum.compiltion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {
    private Integer id;

    private List<EventShortDto> events;

    private Boolean pinned = false;

    @NotBlank(message = "Compilation name could not be blank")
    private String title;
}