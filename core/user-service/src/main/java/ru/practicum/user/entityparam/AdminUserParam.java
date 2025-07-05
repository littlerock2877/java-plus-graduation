package ru.practicum.user.entityparam;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminUserParam {
    private List<Integer> userIds;
    private Integer from;
    private Integer size;
}
