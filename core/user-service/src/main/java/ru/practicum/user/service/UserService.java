package ru.practicum.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.entityparam.AdminUserParam;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    UserDto createUser(UserDto userDto);

    @Transactional
    void deleteUser(Integer userId);

    List<UserDto> getUsers(AdminUserParam userParam);

    UserDto findById(Integer userId);
}