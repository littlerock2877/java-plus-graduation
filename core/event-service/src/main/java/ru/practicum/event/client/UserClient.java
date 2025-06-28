package ru.practicum.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.UserDto;

import java.util.List;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/admin/users/{userId}")
    UserDto findById(@PathVariable Integer userId);

    @GetMapping("/admin/users")
    List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                           @RequestParam(defaultValue = "0") Integer from,
                           @RequestParam(defaultValue = "10") Integer size);
}