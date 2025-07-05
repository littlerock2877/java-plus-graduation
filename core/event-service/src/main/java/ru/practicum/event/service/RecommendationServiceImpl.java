package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.RecommendationDto;
import ru.practicum.ewm.client.RestStatClient;
import ru.practicum.ewm.stats.protobuf.RecommendedEventProto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RestStatClient restStatClient;

    @Override
    public List<RecommendationDto> getRecommendations(long userId, int size) {

        log.info("call analyzerClient.getRecommendedEventForUser: userId = {}, size {}", userId, size);
        List<RecommendedEventProto> recommendations =
                restStatClient.getRecommendationsForUser(userId, size).toList();
        log.info("analyzerClient.getRecommendedEventForUser  finished: userId = {}, size {}", userId, size);
        log.debug("result {}", recommendations);
        List<RecommendationDto> recommendationDtos = new ArrayList<>();
        recommendations.forEach(recommendationProto -> recommendationDtos.add(
                new RecommendationDto(recommendationProto.getEventId(), recommendationProto.getScore())));
        return recommendationDtos;
    }
}