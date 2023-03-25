package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query(" SELECT e " +
            "FROM Event AS e " +
            "WHERE LOWER(e.annotation) LIKE %:text% OR LOWER(e.description) LIKE %:text% " +
            "AND (:categories is null OR e.category.id IN (:categories)) " +
            "AND e.paid = :paid " +
            "AND (:state is null OR e.state = :state) " +
            "AND (coalesce(:start, null) is null OR e.eventDate >= :start) " +
            "AND (coalesce(:end, null) is null OR e.eventDate <= :end)")
    List<Event> findAllByParameters(String text, List<Long> categories, boolean paid, LocalDateTime start,
                                    LocalDateTime end, EventState state, Pageable pageable);

    @Query(" SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users is null OR e.initiator.id IN (:users)) " +
            "AND (:states is null OR e.state IN (:states)) " +
            "AND (:categories is null OR e.category.id IN (:categories)) " +
            "AND (coalesce(:start, null) is null OR e.eventDate >= :start) " +
            "AND (coalesce(:end, null) is null OR e.eventDate <= :end) ")
    List<Event> findAllByParameters(List<Long> users, List<EventState> states, List<Long> categories,
                                    LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query(" SELECT e " +
            "FROM Event AS e " +
            "WHERE e.initiator.id = :uid AND e.eventDate >= :eventDate " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAllByParameters(Long uid, LocalDateTime eventDate, Pageable pageable);

    List<Event> findAllByInitiatorId(Long uid, Pageable pageable);

    @Query(" SELECT e " +
            "FROM Event AS e " +
            "WHERE e.id IN (:eventIds) AND e.eventDate >= :eventDate " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(List<Long> eventIds, LocalDateTime eventDate, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long uid, Long eventId);
}
