package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Like;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndEventId(Integer userId, Integer eventId);

    void deleteByUserIdAndEventId(Integer userId, Integer eventId);

    Long countByEventId(Integer eventId);

    List<Like> findAllByEventId(Integer eventId);

    List<Like> findAllByUserId(Integer userId);
}
