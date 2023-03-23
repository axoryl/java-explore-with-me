package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.UpdateRequestStatusDto;
import ru.practicum.dto.request.UpdateRequestsResultDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.event.EventState;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.mapper.RequestMapper.mapToRequestDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<RequestDto> getAllUserRequests(final Long uid) {
        userRepository.findById(uid).orElseThrow(
                () -> new NotFoundException("User with id=" + uid + " was not found",
                        "The required object was not found."));

        return requestRepository.findAllByRequester(uid).stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RequestDto saveRequest(final Long uid, final Long eventId) {
        final var created = LocalDateTime.now();
        requestRepository.findByRequesterAndEvent(uid, eventId)
                .ifPresent(reqs -> {
                    throw new ConflictException("Request already exists.", "Integrity constraint has been violated.");
                });

        final var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found.",
                        "The required object was not found.")
        );

        userRepository.findById(uid).orElseThrow(
                () -> new NotFoundException("User with id=" + uid + " was not found.",
                        "The required object was not found.")
        );

        if (event.getInitiator().getId().equals(uid)) {
            throw new ConflictException("The event initiator cannot add a request to participate in his event.",
                    "For the requested operation the conditions are not met.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Participation in an unpublished event is not possible.",
                    "For the requested operation the conditions are not met.");
        }

        final var request = Request.builder()
                .requester(uid)
                .created(created)
                .event(eventId)
                .status(RequestStatus.PENDING)
                .build();

        if (event.getRequestModeration()) {
            return mapToRequestDto(requestRepository.save(request));
        }

        final var numberOfRequests = requestRepository.countRequestsByEventAndStatus(eventId, RequestStatus.CONFIRMED);

        if (!event.getParticipantLimit().equals(0)
                && Objects.equals(event.getParticipantLimit(), numberOfRequests)) {
            throw new ConflictException("Participation request limit reached.",
                    "For the requested operation the conditions are not met.");
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            return mapToRequestDto(requestRepository.save(request));
        }
    }

    @Transactional
    @Override
    public RequestDto cancelRequest(final Long uid, final Long requestId) {
        final var request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request with id=" + requestId + " was not found.",
                        "The required object was not found.")
        );

        if (!request.getRequester().equals(uid)) {
            throw new BadRequestException("Only the owner can cancel the request.", "Incorrectly made request.");
        }

        request.setStatus(RequestStatus.CANCELED);

        return mapToRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public UpdateRequestsResultDto updateRequestsStatus(final Long uid, final Long eventId,
                                                        final UpdateRequestStatusDto updateRequestStatusDto) {
        final var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found.",
                        "The required object was not found.")
        );

        final var requests = requestRepository.findAllById(updateRequestStatusDto.getRequestIds()).stream()
                .peek(r -> {
                    if (r.getStatus() != RequestStatus.PENDING)
                        throw new ConflictException("The request must be in PENDING status.",
                                "For the requested operation the conditions are not met.");
                })
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());

        if (updateRequestStatusDto.getStatus() == RequestStatus.REJECTED) {
            requests.forEach(r -> r.setStatus(RequestStatus.REJECTED));
            requestRepository.saveAll(requests.stream()
                    .map(RequestMapper::mapToRequest)
                    .collect(Collectors.toList()));

            return UpdateRequestsResultDto.builder()
                    .rejectedRequests(requests)
                    .build();
        }

        var numberOfRequests = requestRepository.countRequestsByEventAndStatus(
                eventId,
                RequestStatus.CONFIRMED
        );
        final var participantLimit = event.getParticipantLimit();

        if (participantLimit > 0 && Objects.equals(participantLimit, numberOfRequests)) {
            throw new ConflictException("Participation request limit reached.",
                    "For the requested operation the conditions are not met.");
        }

        if (participantLimit.equals(0)) {
            requests.forEach(r -> r.setStatus(RequestStatus.CONFIRMED));
            requestRepository.saveAll(requests.stream()
                    .map(RequestMapper::mapToRequest)
                    .collect(Collectors.toList()));

            return UpdateRequestsResultDto.builder()
                    .confirmedRequests(requests)
                    .build();
        }

        final var confirmedRequests = new ArrayList<RequestDto>();
        final var rejectedRequests = new ArrayList<RequestDto>();

        for (RequestDto request : requests) {
            if (participantLimit > numberOfRequests) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(request);
                numberOfRequests++;
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        }

        requestRepository.saveAll(requests.stream()
                .map(RequestMapper::mapToRequest)
                .collect(Collectors.toList()));

        return UpdateRequestsResultDto.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}
