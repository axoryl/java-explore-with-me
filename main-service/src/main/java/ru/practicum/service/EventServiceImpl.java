package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.StatsShortDto;
import ru.practicum.dto.event.EventCreationDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.UpdateEventDto;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventSort;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.UpdateEventState;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.EventMapper.mapToEvent;
import static ru.practicum.mapper.EventMapper.mapToEventDto;
import static ru.practicum.mapper.LocationMapper.mapToLocation;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventDto> getAllEventsForUser(String text, List<Long> categories, boolean paid,
                                              String rangeStart, String rangeEnd, boolean onlyAvailable,
                                              String sort, int from, int size) {
        final var sortBy = sort != null ? EventSort.valueOf(sort) : null;
        final var searchText = text.trim().toLowerCase();
        final var pageable = PageRequest.of(from / size, size, Sort.by("eventDate"));
        final var start = rangeStart != null ? LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER) : null;
        final var end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER) : null;
        final var events = eventRepository.findAllByParameters(searchText, categories, paid,
                        start, end, EventState.PUBLISHED, pageable).stream()
                .map(EventMapper::mapToEventDto)
                .collect(Collectors.toList());

        if (!events.isEmpty()) {
            setRequests(events);
            setViews(events);
        }

        if (sortBy == EventSort.VIEWS) {
            events.sort(Comparator.comparingLong(EventDto::getViews));
        }

        if (onlyAvailable) {
            return events.stream()
                    .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        return events;
    }

    @Override
    public List<EventDto> getAllEventsForAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, int from, int size) {
        final var start = rangeStart != null ? LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER) : null;
        final var end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER) : null;
        final var pageable = PageRequest.of(from / size, size);
        final var eventStates = states != null
                ? states.stream().map(EventState::valueOf).collect(Collectors.toList())
                : null;

        final var events = eventRepository.findAllByParameters(users, eventStates, categories,
                        start, end, pageable)
                .stream()
                .map(EventMapper::mapToEventDto)
                .collect(Collectors.toList());

        if (states != null && states.contains("PUBLISHED")) {
            setRequests(events);
            setViews(events);
        }
        return events;
    }

    @Override
    public List<EventDto> getUserEvents(Long uid, int from, int size) {
        final var pageable = PageRequest.of(from / size, size);
        final var events = eventRepository.findAllByInitiatorId(uid, pageable).stream()
                .map(EventMapper::mapToEventDto)
                .collect(Collectors.toList());

        final var isPublished = events.stream()
                .anyMatch(e -> e.getState() == EventState.PUBLISHED);

        if (isPublished) {
            setRequests(events);
            setViews(events);
        }

        return events;
    }

    @Override
    public EventDto getUserEventById(final Long uid, final Long eventId) {
        final var event = eventRepository.findByInitiatorIdAndId(uid, eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found",
                        "The required object was not found.")
        );

        if (event.getState() == EventState.PUBLISHED) {
            final var confirmedRequests = requestRepository.countRequestsByEventAndStatus(eventId,
                    RequestStatus.CONFIRMED);
            final var start = DATE_TIME_FORMATTER.format(event.getPublishedOn());
            final var end = DATE_TIME_FORMATTER.format(LocalDateTime.now());
            final var views = statsClient.getViewStats(start, end, List.of("/events/" + eventId), false).stream()
                    .mapToLong(StatsShortDto::getHits)
                    .sum();
            return mapToEventDto(event, views, confirmedRequests);
        }
        return mapToEventDto(event);
    }

    @Override
    public EventDto getEventById(final Long id) {
        final var event = eventRepository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event with id=" + id + " was not found",
                        "The required object was not found.")
        );

        final var confirmedRequests = requestRepository.countRequestsByEventAndStatus(
                event.getId(),
                RequestStatus.CONFIRMED
        );
        final var start = DATE_TIME_FORMATTER.format(event.getPublishedOn());
        final var end = DATE_TIME_FORMATTER.format(LocalDateTime.now());
        final var views = statsClient.getViewStats(start, end, List.of("/events/" + event.getId()), false).stream()
                .mapToLong(StatsShortDto::getHits)
                .sum();

        return mapToEventDto(event, views, confirmedRequests);
    }

    @Override
    public List<RequestDto> getRequests(Long uid, Long eventId) {
        final var event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getInitiator().getId().equals(uid)) {
            throw new BadRequestException("Only the owner can get information about requests.",
                    "Incorrectly made request.");
        }
        return requestRepository.findAllByEvent(eventId).stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDto saveEvent(Long uid, EventCreationDto event) {
        final var createdOn = LocalDateTime.now();

        if (event.getEventDate().isBefore(createdOn.plusHours(2))) {
            throw new ConflictException(
                    "Field: eventDate. Error: must contain a date that has not yet arrived " +
                            "Value: " + event.getEventDate(),
                    "For the requested operation the conditions are not met.");
        }

        final var location = locationRepository.save(mapToLocation(event.getLocation()));
        final var user = userRepository.getReferenceById(uid);
        final var category = categoryRepository.getReferenceById(event.getCategory());
        final var eventToSave = mapToEvent(event, createdOn, user, category, location);

        return mapToEventDto(eventRepository.save(eventToSave));
    }

    @Transactional
    @Override
    public EventDto updateEventByAdmin(Long eventId, UpdateEventDto updatedEvent) {
        final var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found",
                        "The required object was not found.")
        );

        if (updatedEvent.getStateAction() == UpdateEventState.PUBLISH_EVENT
                && event.getState() != EventState.PENDING) {
            throw new ConflictException(
                    "Cannot publish the event because it's not in the right state: " + event.getState(),
                    "For the requested operation the conditions are not met.");
        }
        if (updatedEvent.getStateAction() == UpdateEventState.REJECT_EVENT
                && event.getState() == EventState.PUBLISHED) {
            throw new ConflictException(
                    "Cannot reject the event because it's not in the right state: " + event.getState(),
                    "For the requested operation the conditions are not met.");
        }
        if (updatedEvent.getEventDate() != null
                && updatedEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException(
                    "The start date of the edited event must be no earlier than one hour from the publication date",
                    "For the requested operation the conditions are not met.");
        }

        updateEventFields(event, updatedEvent);
        return mapToEventDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventDto updateEventByUser(Long uid, Long eventId, UpdateEventDto updatedEvent) {
        final var event = eventRepository.findByInitiatorIdAndId(uid, eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found",
                        "The required object was not found.")
        );

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed",
                    "For the requested operation the conditions are not met.");
        }

        if (updatedEvent.getEventDate() != null
                && updatedEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException(
                    "The start date of the edited event must be no earlier than two hour from the publication date",
                    "For the requested operation the conditions are not met.");
        }

        updateEventFields(event, updatedEvent);
        return mapToEventDto(eventRepository.save(event));
    }

    private void setRequests(final List<EventDto> events) {
        final var requests = requestRepository.findAllById(events.stream()
                .map(EventDto::getId)
                .collect(Collectors.toList()));

        final var numberOfEventRequests = requests.stream()
                .filter(r -> r.getStatus() == RequestStatus.CONFIRMED)
                .collect(Collectors.groupingBy(Request::getEvent, Collectors.counting()));

        events.forEach(e -> e.setConfirmedRequests(numberOfEventRequests.getOrDefault(e.getId(), 0L)));
    }

    private void setViews(final List<EventDto> events) {
        final var start = DATE_TIME_FORMATTER.format(events.get(0).getEventDate());
        final var end = DATE_TIME_FORMATTER.format(LocalDateTime.now());

        final List<String> uris = new ArrayList<>();
        events.forEach(e -> uris.add("/events/" + e.getId()));

        final var views = statsClient.getViewStats(start, end, uris, false).stream()
                .collect(Collectors.toMap(o -> Long.parseLong(o.getUri().substring(8)), StatsShortDto::getHits));

        events.forEach(e -> e.setViews(views.getOrDefault(e.getId(), 0L)));
    }

    private void updateEventFields(final Event eventToUpdate, final UpdateEventDto updateEventDto) {
        if (updateEventDto.getAnnotation() != null && !updateEventDto.getAnnotation().isBlank()) {
            eventToUpdate.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getCategory() != null) {
            final var category = categoryRepository.findById(updateEventDto.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category with id=" + updateEventDto.getCategory() + " was not found",
                            "Incorrectly made request.")
            );
            eventToUpdate.setCategory(category);
        }
        if (updateEventDto.getDescription() != null && !updateEventDto.getDescription().isBlank()) {
            eventToUpdate.setDescription(updateEventDto.getDescription());
        }
        if (updateEventDto.getEventDate() != null) {
            eventToUpdate.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getLocation() != null) {
            eventToUpdate.setLocation(mapToLocation(updateEventDto.getLocation()));
        }
        if (updateEventDto.getPaid() != null) {
            eventToUpdate.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (updateEventDto.getTitle() != null && !updateEventDto.getTitle().isBlank()) {
            eventToUpdate.setTitle(updateEventDto.getTitle());
        }

        switch (updateEventDto.getStateAction()) {
            case SEND_TO_REVIEW:
                eventToUpdate.setState(EventState.PENDING);
                break;
            case CANCEL_REVIEW:
            case REJECT_EVENT:
                eventToUpdate.setState(EventState.CANCELED);
                break;
            case PUBLISH_EVENT:
                eventToUpdate.setState(EventState.PUBLISHED);
                eventToUpdate.setPublishedOn(LocalDateTime.now());
                break;
        }
    }
}
