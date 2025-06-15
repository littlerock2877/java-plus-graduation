package client;

import exception.InvalidRequestException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RestStatClient implements StatClient {
    private final DiscoveryClient discoveryClient;

    @Value("${service.stats-server}")
    private String statServerId;

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        URI serviceUri = getStatsServiceUri();
        RestClient.create().post()
                .uri("%s/hit".formatted(serviceUri))
                .contentType(MediaType.APPLICATION_JSON)
                .body(endpointHitDto)
                .retrieve()
                .onStatus(status -> status != HttpStatus.CREATED, (request, response) -> {
                    throw new InvalidRequestException(response.getStatusCode().value() + ": " + response.getBody());
                });
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        RestClient client = RestClient.builder()
                .baseUrl(getStatsServiceUri().toString())
                .build();

        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .onStatus(status -> status != HttpStatus.OK, (request, response) -> {
                    throw new InvalidRequestException(response.getStatusCode().value() + ": " + response.getBody());
                })
                .body(new ParameterizedTypeReference<List<ViewStatsDto>>() {});
    }

    private URI getStatsServiceUri() {
        List<ServiceInstance> instances = discoveryClient.getInstances(statServerId);
        if (instances.isEmpty()) {
            throw new IllegalStateException("Service %s not found in Eureka".formatted(statServerId));
        }
        ServiceInstance serviceInstance = instances.getFirst();
        return serviceInstance.getUri();
    }
}