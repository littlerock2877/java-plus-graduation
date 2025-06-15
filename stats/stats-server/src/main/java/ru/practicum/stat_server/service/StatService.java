package ru.practicum.stat_server.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import java.util.List;

@Transactional(readOnly = true)
public interface StatService {
    List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

    @Transactional
    void saveHit(EndpointHitDto endpointHitDto);
}