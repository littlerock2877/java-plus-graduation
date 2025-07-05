package ru.practicum.analyzer.service;

import ru.practicum.analyzer.entity.EventSimilarity;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public interface EventSimilarityService {
    EventSimilarity save(EventSimilarityAvro eventSimilarityAvro);
}