package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.practicum.main_service.event.model.Location;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Integer category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;

    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}