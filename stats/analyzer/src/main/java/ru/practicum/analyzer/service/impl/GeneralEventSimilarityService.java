package ru.practicum.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.analyzer.entity.EventSimilarity;
import ru.practicum.analyzer.repository.EventSimilarityRepository;
import ru.practicum.analyzer.service.EventSimilarityService;
import ru.practicum.analyzer.service.mapper.EventSimilarityMapper;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

@Service
@RequiredArgsConstructor
public class GeneralEventSimilarityService implements EventSimilarityService {

    private final EventSimilarityRepository eventSimilarityRepository;
    private final EventSimilarityMapper eventSimilarityMapper;

    @Override
    public EventSimilarity save(EventSimilarityAvro eventSimilarityAvro) {
        return eventSimilarityRepository.save(
                eventSimilarityMapper.toEventSimilarity(eventSimilarityAvro));
    }

}