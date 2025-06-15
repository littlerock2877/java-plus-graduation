package ru.practicum.main_service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.main_service.event.model.Like;
import ru.practicum.main_service.event.model.LikeId;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    boolean existsByUserIdAndEventId(Integer userId, Integer eventId);

    void deleteByUserIdAndEventId(Integer userId, Integer eventId);

    Long countByEventId(Integer eventId);

    List<Like> findAllByEventId(Integer eventId);

    List<Like> findAllByUserId(Integer userId);
}
