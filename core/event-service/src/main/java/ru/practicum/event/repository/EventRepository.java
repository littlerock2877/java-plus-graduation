package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.categories.model.Category;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiatorId(Integer userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Integer eventId, Integer userId);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiatorId IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> findAdminEvents(@Param("users") List<Integer> users,
                                @Param("states") List<String> states,
                                @Param("categories") List<Integer> categories,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable page);

    @Query("SELECT e FROM Event e WHERE " +
            "(COALESCE(:text, '') = '' OR " +
            "LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.title) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND (:onlyAvailable IS NULL OR e.state = ru.practicum.event.enums.EventState.PUBLISHED)")
    Page<Event> findPublicEvents(
            @Param("text") String text,
            @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable);

    Optional<Event> findByCategory(Category category);

    List<Event> findAllByIdIsIn(Collection<Integer> ids);
}