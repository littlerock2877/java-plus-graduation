package ru.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.analyzer.model.UserActionHistory;

import java.util.List;

public interface UserActionHistoryRepository extends JpaRepository<UserActionHistory, Long> {
    List<UserActionHistory> findByUserId(Long userId);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
}