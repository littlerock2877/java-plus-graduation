package ru.practicum.stat_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stat_server.exception.EventDateValidationException;
import ru.practicum.stat_server.mapper.HitMapper;
import ru.practicum.stat_server.mapper.ViewStatsMapper;
import ru.practicum.stat_server.repository.StatRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final HitMapper hitMapper;
    private final ViewStatsMapper statsMapper;

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);

        if (startDateTime.isAfter(endDateTime)) {
            throw new EventDateValidationException("Start date should be before end");
        }

        if (unique && uris != null) {
            return statsMapper.toDtoList(statRepository.findDistinctViews(startDateTime, endDateTime, uris));
        } else if (unique) {
            return statsMapper.toDtoList(statRepository.findDistinctViews(startDateTime, endDateTime));
        } else if (uris != null) {
            return statsMapper.toDtoList(statRepository.findViews(startDateTime, endDateTime, uris));
        } else {
            return statsMapper.toDtoList(statRepository.findViews(startDateTime, endDateTime));
        }
    }

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        statRepository.save(hitMapper.toModel(endpointHitDto));
    }
}