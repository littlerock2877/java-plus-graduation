package ru.practicum.main_service.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiatorId(Integer userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Integer eventId, Integer userId);

    @Query("""
            SELECT e FROM Event e
            WHERE (:users IS NULL OR e.initiator.id IN :users)
            AND (:states IS NULL OR e.state IN :states)
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND e.eventDate BETWEEN :rangeStart AND :rangeEnd
            """)
    List<Event> findAdminEvents(List<Integer> users,
                                List<String> states,
                                List<Integer> categories,
                                LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                Pageable page);

    @Query("""
            SELECT e FROM Event e
            WHERE (?1 IS NULL OR (e.title ILIKE ?1
            OR e.description ILIKE ?1
            OR e.annotation ILIKE ?1))
            AND (?2 IS NULL OR e.category.id IN ?2)
            AND (?3 IS NULL OR e.paid = ?3)
            AND e.eventDate BETWEEN ?4 AND ?5
            AND (?6 IS NULL OR e.state = 'PUBLISHED')
            """)
    List<Event> findPublicEvents(String text,
                                 List<Integer> categories,
                                 Boolean paid,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 Boolean onlyAvailable,
                                 Pageable pageable);

    Optional<Event> findByCategory(Category category);
}