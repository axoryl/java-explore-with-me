package ru.practicum.service;

import ru.practicum.dto.event.EventCreationDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.UpdateEventDto;
import ru.practicum.dto.request.RequestDto;

import java.util.List;

public interface EventService {

    List<EventDto> getAllEventsForUser(String text, List<Long> categories, boolean paid,
                                       String rangeStart, String rangeEnd, boolean onlyAvailable,
                                       String sort, int from, int size);

    List<EventDto> getAllEventsForAdmin(List<Long> users, List<String> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, int from, int size);

    List<EventDto> getUserEvents(Long uid, int from, int size);

    EventDto getUserEventById(Long uid, Long eventId);

    EventDto getEventById(Long id);

    List<RequestDto> getRequests(Long uid, Long eventId);

    EventDto saveEvent(Long uid, EventCreationDto event);

    EventDto updateEventByAdmin(Long eventId, UpdateEventDto updatedEvent);

    EventDto updateEventByUser(Long uid, Long eventId, UpdateEventDto updatedEvent);
}
