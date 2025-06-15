package ru.practicum.main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.request.model.Request;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("select p from Request as p " +
            "join Event as e ON p.event = e.id " +
            "where p.event = :eventId and e.initiator.id = :userId")
    List<Request> findAllByEventWithInitiator(@Param(value = "userId") Integer userId, @Param("eventId") Integer eventId);

    List<Request> findAllByRequester(Integer userId);

    Optional<Request> findByRequesterAndId(Integer userId, Integer requestId);

    Boolean existsByRequesterAndEvent(Integer userId, Integer eventId);

    List<Request> findAllByEvent(Integer eventId);
}