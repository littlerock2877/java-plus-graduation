package ru.practicum.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.request.dto.UserDto;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/{userId}")
    UserDto findById(Integer userId);
}