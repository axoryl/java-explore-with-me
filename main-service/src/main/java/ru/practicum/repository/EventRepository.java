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
            "AND e.category.id IN (:categories) " +
            "AND e.paid = :paid AND e.state = :state " +
            "AND e.eventDate >= :start AND e.eventDate <= :end")
    List<Event> findAllByParameters(String text, List<Long> categories, boolean paid, LocalDateTime start,
                                    LocalDateTime end, EventState state, Pageable pageable);

    @Query(" SELECT e " +
            "FROM Event AS e " +
            "WHERE e.initiator.id IN(:users) AND e.state IN (:states) AND e.category.id IN (:categories) " +
            "AND e.eventDate >= :start AND e.eventDate <= :end")
    List<Event> findAllByParameters(List<Long> users, List<EventState> states, List<Long> categories,
                                    LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Event> findAllByInitiatorId(Long uid, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long uid, Long eventId);
}
