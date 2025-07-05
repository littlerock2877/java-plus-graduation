package ru.practicum.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.analyzer.entity.UserAction;
import ru.practicum.analyzer.repository.UserActionRepository;
import ru.practicum.analyzer.service.UserActionService;
import ru.practicum.analyzer.service.mapper.UserActionMapper;
import ru.practicum.ewm.stats.avro.UserActionAvro;

@Service
@RequiredArgsConstructor
public class GeneralUserActionsService implements UserActionService {
    private final UserActionRepository userActionRepository;
    private final UserActionMapper userActionMapper;

    @Override
    public UserAction save(UserActionAvro userActionAvro) {
        return userActionRepository.save(
                userActionMapper.toUserAction(userActionAvro)
        );
    }
}