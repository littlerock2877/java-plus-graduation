package ru.practicum.main_service.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.entityParam.AdminUserParam;
import java.util.List;

@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    UserDto createUser(UserDto userDto);

    @Transactional
    void deleteUser(Integer userId);

    List<UserDto> getUsers(AdminUserParam userParam);
}