package ru.practicum.main_service.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.entityParam.AdminUserParam;
import ru.practicum.main_service.user.service.UserService;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Getting users with ids {} - Started", ids);
        AdminUserParam userParam = new AdminUserParam(ids, from, size);
        List<UserDto> users = userService.getUsers(userParam);
        log.info("Getting users with ids {} - Finished", ids);
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Creating user {} - Started", userDto);
        UserDto user = userService.createUser(userDto);
        log.info("Creating user {} - Finished", userDto);
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Deleting user with id {} - Started", userId);
        userService.deleteUser(userId);
        log.info("Deleting user with id {} - Finished", userId);
    }
}