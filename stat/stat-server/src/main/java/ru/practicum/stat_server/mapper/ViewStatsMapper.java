package ru.practicum.stat_server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stat_server.model.ViewStats;
import java.util.List;

@Component
public class ViewStatsMapper {
    public ViewStatsDto toDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }

    public List<ViewStatsDto> toDtoList(List<ViewStats> viewStats) {
        return viewStats.stream().map(this::toDto).toList();
    }
}
