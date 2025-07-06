package ru.practicum.event.service;

import ru.practicum.event.dto.RecommendationDto;
import java.util.List;

public interface RecommendationService {
    List<RecommendationDto> getRecommendations(long userId, int size);
}