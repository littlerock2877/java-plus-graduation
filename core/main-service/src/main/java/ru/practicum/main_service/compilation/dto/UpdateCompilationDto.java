package ru.practicum.main_service.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Collection;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {
    @UniqueElements(message = "All events must be unique")
    Collection<Integer> events;
    boolean pinned;
    @Size(min = 1, max = 50)
    String title;
}
