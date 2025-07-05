package ru.practicum.analyzer.service;

import ru.practicum.analyzer.entity.RecommendedEvent;
import java.util.List;

public interface RecommendationService {
    List<RecommendedEvent> getRecommendedEventsForUser(long userId, int maxValue);
    List<RecommendedEvent> getSimilarEvents(long eventId, long userId, long maxValue);
    List<RecommendedEvent> getInteractionsCount(List<Long> eventsIds);
}