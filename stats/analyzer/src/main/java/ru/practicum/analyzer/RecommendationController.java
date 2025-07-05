package ru.practicum.analyzer;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.stats.recommendations.proto.RecommendationMessages;
import ru.yandex.practicum.grpc.stats.recommendations.proto.RecommendationsControllerGrpc;


@GrpcService
@RequiredArgsConstructor
public class RecommendationController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {
    private final RecommendationService recommendationService;

    @Override
    public void getSimilarEvents(
            RecommendationMessages.SimilarEventsRequestProto request,
            StreamObserver<RecommendationMessages.RecommendedEventProto> responseObserver
    ) {
        recommendationService.getSimilarEvents(request, responseObserver);
    }

    @Override
    public void getRecommendationsForUser(
            RecommendationMessages.UserPredictionsRequestProto request,
            StreamObserver<RecommendationMessages.RecommendedEventProto> responseObserver
    ) {
        recommendationService.getRecommendationsForUser(request, responseObserver);
    }

    @Override
    public void getInteractionsCount(
            RecommendationMessages.InteractionsCountRequestProto request,
            StreamObserver<RecommendationMessages.RecommendedEventProto> responseObserver
    ) {
        recommendationService.getInteractionsCount(request, responseObserver);
    }
}