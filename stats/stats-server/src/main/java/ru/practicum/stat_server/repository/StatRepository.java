package ru.practicum.stat_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat_server.model.EndpointHit;
import ru.practicum.stat_server.model.ViewStats;
import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.stat_server.model.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "AND eh.uri IN (?3) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (DISTINCT eh.ip) DESC")
    List<ViewStats> findDistinctViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stat_server.model.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (DISTINCT eh.ip) DESC")
    List<ViewStats> findDistinctViews(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stat_server.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "AND eh.uri IN (?3) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC")
    List<ViewStats> findViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stat_server.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC")
    List<ViewStats> findViews(LocalDateTime start, LocalDateTime end);
}