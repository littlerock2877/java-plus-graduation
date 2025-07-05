package ru.practicum.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.entityparam.AdminUserParam;
import ru.practicum.user.service.UserService;

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

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable Integer userId) {
        log.debug("Finding user with id {} - Started", userId);
        UserDto foundUser = userService.findById(userId);
        log.info("Finding user with id {} - Started", userId);
        return foundUser;
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