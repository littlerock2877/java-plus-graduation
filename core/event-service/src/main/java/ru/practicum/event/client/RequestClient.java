package ru.practicum.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.dto.UserDto;

@FeignClient(name = "request-service", configuration = FeignConfig.class)
public interface RequestClient {
    @GetMapping("/{userId}")
    UserDto findById(Integer userId);
}