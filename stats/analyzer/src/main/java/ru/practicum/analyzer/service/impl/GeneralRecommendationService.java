package ru.practicum.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.analyzer.configuration.UserActionWeight;
import ru.practicum.analyzer.entity.ActionType;
import ru.practicum.analyzer.entity.EventSimilarity;
import ru.practicum.analyzer.entity.RecommendedEvent;
import ru.practicum.analyzer.entity.UserAction;
import ru.practicum.analyzer.repository.EventSimilarityRepository;
import ru.practicum.analyzer.repository.UserActionRepository;
import ru.practicum.analyzer.service.RecommendationService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneralRecommendationService implements RecommendationService {

    private final UserActionRepository userActionRepository;
    private final EventSimilarityRepository similarityRepository;

    @Override
    public List<RecommendedEvent> getRecommendedEventsForUser(long userId, int maxValue) {
        List<UserAction> actionsOfUser = userActionRepository.getUserActionsByUserId(userId);

        if (actionsOfUser == null || actionsOfUser.isEmpty()) {
            return Collections.emptyList();
        }

        actionsOfUser.sort(((UserAction o1, UserAction o2) -> o2.getTimestamp().compareTo(o1.getTimestamp())));

        List<UserAction> actions = actionsOfUser.subList(0, Math.min(maxValue, actionsOfUser.size()));
        Set<Long> eventIdsUserAlreadyInteracted =
                userActionRepository.getUserActionsByUserId(userId).stream()
                        .map(UserAction::getEventId)
                        .collect(Collectors.toSet());

        Map<Long, Long> recommendationSimMap = new HashMap<>();

        Set<Long> actionEventIds = actions.stream()
                .map(UserAction::getEventId)
                .collect(Collectors.toSet());

        List<EventSimilarity> allSimilarities = similarityRepository.findAllByEventAIdInOrEventBIdIn(actionEventIds, actionEventIds);

        for (UserAction action : actions) {
            for (EventSimilarity eventSimilarity : allSimilarities) {
                long eventIdRecomm = (action.getEventId() != eventSimilarity.getEventAId())
                        ? eventSimilarity.getEventAId()
                        : eventSimilarity.getEventBId();
                if (eventIdsUserAlreadyInteracted.contains(eventIdRecomm)) {
                    continue;
                }
                double currentSim = recommendationSimMap.getOrDefault(eventIdRecomm, 0L);
                if (currentSim <= eventSimilarity.getScore()) {
                    recommendationSimMap.put(eventIdRecomm, (long) eventSimilarity.getScore());
                }
            }
        }

        return recommendationSimMap.entrySet().stream()
                .map(entry -> RecommendedEvent.builder()
                        .eventId(entry.getKey())
                        .score(entry.getValue())
                        .build())
                .sorted(Comparator.comparingDouble(RecommendedEvent::getScore).reversed())
                .limit(maxValue)
                .toList();
    }

    @Override
    public List<RecommendedEvent> getSimilarEvents(long eventId, long userId, long maxValue) {
        List<EventSimilarity> similarityList = similarityRepository.findAllByEventAIdOrEventBId(eventId, eventId);

        Set<Long> eventIdsUserAlreadyInteracted =
                userActionRepository.getUserActionsByUserId(userId).stream()
                        .map(UserAction::getEventId)
                        .collect(Collectors.toSet());

        List<RecommendedEvent> recommendedEvents = new ArrayList<>();

        for (EventSimilarity eventSimilarity : similarityList) {
            long eventIdRecomm = (eventSimilarity.getEventAId() != eventId)
                    ? eventSimilarity.getEventAId() : eventSimilarity.getEventBId();

            if (!eventIdsUserAlreadyInteracted.contains(eventIdRecomm)) {
                recommendedEvents.add(RecommendedEvent.builder()
                        .eventId(eventIdRecomm)
                        .score(eventSimilarity.getScore())
                        .build());
            }
        }

        return recommendedEvents.stream()
                .sorted(Comparator.comparingDouble(RecommendedEvent::getScore).reversed())
                .limit(maxValue)
                .toList();
    }

    @Override
    public List<RecommendedEvent> getInteractionsCount(List<Long> eventsIds) {

        List<RecommendedEvent> recommendedEvents = new ArrayList<>();

        for (Long eventId : eventsIds) {
            List<UserAction> actionsList = userActionRepository.getUserActionsByEventId(eventId);
            double weightSum = 0;
            for (UserAction action : actionsList) {
                weightSum += getWeightFromActionType(action.getActionType());
            }
            recommendedEvents.add(RecommendedEvent.builder()
                    .eventId(eventId)
                    .score(weightSum)
                    .build());
        }

        return recommendedEvents.stream()
                .sorted(Comparator.comparingDouble(RecommendedEvent::getScore).reversed())
                .toList();
    }

    private double getWeightFromActionType(ActionType actionType) {
        switch (actionType) {
            case VIEW -> {
                return UserActionWeight.view;
            }
            case REGISTER -> {
                return UserActionWeight.register;
            }
            case LIKE -> {
                return UserActionWeight.like;
            }
            default -> throw new IllegalStateException("Unexpected value: " + actionType);
        }
    }
}