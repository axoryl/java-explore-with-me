package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester(Long uid);

    List<Request> findAllByRequesterAndStatus(Long uid, RequestStatus status);

    Optional<Request> findByRequesterAndEvent(Long uid, Long eventId);

    List<Request> findAllByEvent(Long eventId);

    Integer countRequestsByEventAndStatus(Long eventId, RequestStatus status);
}
