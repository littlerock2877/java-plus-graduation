package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.entityparam.AdminUserParam;
import ru.practicum.user.exception.NotFoundException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(userMapper.toModel(userDto));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto findById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        return userMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getUsers(AdminUserParam userParam) {
        Pageable page = PageRequest.of(userParam.getFrom() / userParam.getSize(), userParam.getSize());
        return userParam.getUserIds() != null && !userParam.getUserIds().isEmpty() ?
                userRepository.findAllById(userParam.getUserIds()).stream().map(userMapper::toUserDto).toList() :
                userRepository.findAll(page).stream().map(userMapper::toUserDto).toList();
    }
}