package ru.practicum.analyzer;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.analyzer.model.EventSimilarity;
import ru.practicum.analyzer.model.UserActionHistory;
import ru.practicum.analyzer.repository.EventSimilarityRepository;
import ru.practicum.analyzer.repository.UserActionHistoryRepository;
import ru.yandex.practicum.grpc.stats.recommendations.proto.RecommendationMessages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final EventSimilarityRepository eventSimilarityRepository;
    private final UserActionHistoryRepository userActionHistoryRepository;

    public void getSimilarEvents(
            RecommendationMessages.SimilarEventsRequestProto request,
            StreamObserver<RecommendationMessages.RecommendedEventProto> responseObserver
    ) {
        List<EventSimilarity> similarities = eventSimilarityRepository.findByEventAOrEventB(request.getEventId(), request.getEventId());

        Set<Long> userInteractions = userActionHistoryRepository.findByUserId(request.getUserId())
                .stream()
                .map(UserActionHistory::getEventId)
                .collect(Collectors.toSet());

        similarities.stream()
                .filter(sim -> !userInteractions.contains(sim.getEventA()) || !userInteractions.contains(sim.getEventB()))
                .sorted(Comparator.comparing(EventSimilarity::getScore).reversed())
                .limit(request.getMaxResults())
                .forEach(sim -> {
                    long recommendedEvent = sim.getEventA().equals(request.getEventId()) ? sim.getEventB() : sim.getEventA();
                    responseObserver.onNext(RecommendationMessages.RecommendedEventProto.newBuilder()
                            .setEventId(recommendedEvent)
                            .setScore(sim.getScore())
                            .build());
                });

        responseObserver.onCompleted();
    }

    public void getRecommendationsForUser(
            RecommendationMessages.UserPredictionsRequestProto request,
            StreamObserver<RecommendationMessages.RecommendedEventProto> responseObserver
    ) {
        List<UserActionHistory> interactions = userActionHistoryRepository.findByUserId(request.getUserId());

        if (interactions.isEmpty()) {
            responseObserver.onCompleted();
            return;
        }

        Set<Long> recentEvents = interactions.stream()
                .sorted(Comparator.comparing(UserActionHistory::getTimestamp).reversed())
                .limit(request.getMaxResults())
                .map(UserActionHistory::getEventId)
                .collect(Collectors.toSet());

        List<EventSimilarity> similarities = new ArrayList<>();
        for (Long eventId : recentEvents) {
            similarities.addAll(eventSimilarityRepository.findByEventAOrEventB(eventId, eventId));
        }

        Set<Long> userInteractions = interactions.stream().map(UserActionHistory::getEventId).collect(Collectors.toSet());

        similarities.stream()
                .filter(sim -> !userInteractions.contains(sim.getEventA()) || !userInteractions.contains(sim.getEventB()))
                .sorted(Comparator.comparing(EventSimilarity::getScore).reversed())
                .limit(request.getMaxResults())
                .forEach(sim -> {
                    long recommendedEvent = userInteractions.contains(sim.getEventA()) ? sim.getEventB() : sim.getEventA();
                    responseObserver.onNext(RecommendationMessages.RecommendedEventProto.newBuilder()
                            .setEventId(recommendedEvent)
                            .setScore(sim.getScore())
                            .build());
                });

        responseObserver.onCompleted();
    }

    public void getInteractionsCount(
            RecommendationMessages.InteractionsCountRequestProto request,
            StreamObserver<RecommendationMessages.RecommendedEventProto> responseObserver
    ) {
        for (long eventId : request.getEventIdList()) {
            double totalWeight = userActionHistoryRepository.findByUserId(eventId)
                    .stream()
                    .mapToDouble((uah) -> switch (uah.getActionType()) {
                        case VIEW -> 0.4;
                        case REGISTER -> 0.8;
                        case LIKE -> 1.0;
                    })
                    .sum();

            responseObserver.onNext(RecommendationMessages.RecommendedEventProto.newBuilder()
                    .setEventId(eventId)
                    .setScore((float) totalWeight)
                    .build());
        }

        responseObserver.onCompleted();
    }
}