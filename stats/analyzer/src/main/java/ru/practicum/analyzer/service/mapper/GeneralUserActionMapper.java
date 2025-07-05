package ru.practicum.analyzer.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.analyzer.entity.ActionType;
import ru.practicum.analyzer.entity.UserAction;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

@Component
public class GeneralUserActionMapper implements UserActionMapper {
    @Override
    public UserAction toUserAction(UserActionAvro actionAvro) {
        return UserAction.builder()
                .userId(actionAvro.getUserId())
                .eventId(actionAvro.getEventId())
                .actionType(toActionType(actionAvro.getActionType()))
                .timestamp(actionAvro.getTimestamp())
                .build();
    }

    private ActionType toActionType(ActionTypeAvro actionTypeAvro) {
        switch (actionTypeAvro) {
            case VIEW -> {
                return ActionType.VIEW;
            }
            case REGISTER -> {
                return ActionType.REGISTER;
            }
            case LIKE -> {
                return ActionType.LIKE;
            }
            default -> throw new RuntimeException("actionTypeAvro: " + actionTypeAvro + " is not supported");
        }
    }
}