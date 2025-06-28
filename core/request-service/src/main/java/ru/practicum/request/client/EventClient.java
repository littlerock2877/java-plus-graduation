package ru.practicum.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.request.dto.EventFullDto;

@FeignClient(name = "event-service", configuration = FeignConfig.class)
public interface EventClient {
    @GetMapping("/admin/events/{eventId}")
    EventFullDto findById(Integer eventId);
}