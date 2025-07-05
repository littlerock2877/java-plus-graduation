package ru.practicum.analyzer.service.mapper;

import ru.practicum.analyzer.entity.UserAction;
import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionMapper {
    UserAction toUserAction(final UserActionAvro actionAvro);
}