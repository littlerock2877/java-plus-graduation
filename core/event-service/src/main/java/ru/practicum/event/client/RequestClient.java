package ru.practicum.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "request-service", configuration = FeignConfig.class)
public interface RequestClient {
    @GetMapping("/admin/requests/count")
    Long getConfirmedRequestsCount(@RequestParam Integer eventId);
}