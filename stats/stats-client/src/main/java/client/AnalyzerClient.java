package client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.stats.recommendations.proto.RecommendationMessages;
import ru.yandex.practicum.grpc.stats.recommendations.proto.RecommendationsControllerGrpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnalyzerClient {
    @GrpcClient("analyzer")
    private RecommendationsControllerGrpc.RecommendationsControllerBlockingStub analyzerStub;

    public List<RecommendationMessages.RecommendedEventProto> getRecommendations(long userId, int maxResults) {
        RecommendationMessages.UserPredictionsRequestProto request = RecommendationMessages.UserPredictionsRequestProto.newBuilder()
                .setUserId(userId)
                .setMaxResults(maxResults)
                .build();

        List<RecommendationMessages.RecommendedEventProto> recommendations = new ArrayList<>();
        analyzerStub.getRecommendationsForUser(request)
                .forEachRemaining(recommendations::add);

        return recommendations;
    }

    public List<RecommendationMessages.RecommendedEventProto> getSimilarEvents(long eventId, long userId, int maxResults) {
        RecommendationMessages.SimilarEventsRequestProto request = RecommendationMessages.SimilarEventsRequestProto.newBuilder()
                .setEventId(eventId)
                .setUserId(userId)
                .setMaxResults(maxResults)
                .build();

        List<RecommendationMessages.RecommendedEventProto> similarEvents = new ArrayList<>();
        analyzerStub.getSimilarEvents(request)
                .forEachRemaining(similarEvents::add);

        return similarEvents;
    }

    public Map<Long, Double> getInteractionsCount(List<Long> eventIds) {
        RecommendationMessages.InteractionsCountRequestProto request = RecommendationMessages.InteractionsCountRequestProto.newBuilder()
                .addAllEventId(eventIds)
                .build();

        Map<Long, Double> interactionsCount = new HashMap<>();
        analyzerStub.getInteractionsCount(request)
                .forEachRemaining(event -> interactionsCount.put(event.getEventId(), (double) event.getScore()));
        return interactionsCount;
    }
}