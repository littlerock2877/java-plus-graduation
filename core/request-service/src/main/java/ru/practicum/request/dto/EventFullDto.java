package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventFullDto {
    private Integer id;
    private String annotation;
    private Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;
    private Integer initiator;
    private Boolean paid;
    private String title;
    private Double rating;
    private LocalDateTime createdOn;
    private String description;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
}