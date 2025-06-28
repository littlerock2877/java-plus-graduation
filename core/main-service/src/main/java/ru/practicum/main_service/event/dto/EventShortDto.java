package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.user.dto.UserShortDto;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventShortDto {
    private Integer id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}