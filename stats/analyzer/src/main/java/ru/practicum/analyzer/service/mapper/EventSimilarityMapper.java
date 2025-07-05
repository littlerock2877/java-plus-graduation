package ru.practicum.analyzer.service.mapper;

import ru.practicum.analyzer.entity.EventSimilarity;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public interface EventSimilarityMapper {
    EventSimilarity toEventSimilarity(EventSimilarityAvro eventSimilarityAvro);
}