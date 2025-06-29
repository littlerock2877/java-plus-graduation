package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByRequester(Integer userId);

    Optional<Request> findByRequesterAndId(Integer userId, Integer requestId);

    Boolean existsByRequesterAndEvent(Integer userId, Integer eventId);

    List<Request> findAllByEvent(Integer eventId);

    List<Request> findAllByEventIn(List<Integer> eventIds);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.event = :eventId AND r.status = 'CONFIRMED'")
    Long countConfirmedRequests(Integer eventId);
}