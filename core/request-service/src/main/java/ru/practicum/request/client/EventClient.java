package ru.practicum.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.request.dto.EventFullDto;
import ru.practicum.request.dto.EventShortDto;

import java.util.List;

@FeignClient(name = "event-service", configuration = FeignConfig.class)
public interface EventClient {
    @GetMapping("/admin/events/{eventId}")
    EventFullDto findById(@PathVariable("eventId") Integer eventId);

    @GetMapping("/users/{userId}/events")
    List<EventShortDto> getEventsByUser(@PathVariable Integer userId,
                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size);
}