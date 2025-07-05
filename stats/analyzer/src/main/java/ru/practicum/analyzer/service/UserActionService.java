package ru.practicum.analyzer.service;

import ru.practicum.analyzer.entity.UserAction;
import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionService {
    UserAction save(UserActionAvro userActionAvro);
}