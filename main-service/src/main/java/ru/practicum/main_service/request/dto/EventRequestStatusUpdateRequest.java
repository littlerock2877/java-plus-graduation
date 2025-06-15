package ru.practicum.main_service.request.dto;

import lombok.*;
import ru.practicum.main_service.request.enums.RequestStatus;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatus status;
}
