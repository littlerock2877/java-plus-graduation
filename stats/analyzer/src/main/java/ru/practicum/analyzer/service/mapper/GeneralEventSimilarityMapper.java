package ru.practicum.analyzer.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.analyzer.entity.EventSimilarity;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

@Component
public class GeneralEventSimilarityMapper implements EventSimilarityMapper {
    @Override
    public EventSimilarity toEventSimilarity(EventSimilarityAvro eventSimilarityAvro) {
        return EventSimilarity.builder()
                .eventAId(eventSimilarityAvro.getEventA())
                .eventBId(eventSimilarityAvro.getEventB())
                .score(eventSimilarityAvro.getScore())
                .timestamp(eventSimilarityAvro.getTimestamp())
                .build();
    }
}