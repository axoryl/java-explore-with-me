package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventCreationDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.UpdateEventDto;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.service.EventService;
import ru.practicum.util.StringTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateEventController {

    private final EventService eventService;
    private final String logTemplate = StringTemplate.PRIVATE_EVENT_LOG;

    @GetMapping("/{uid}/events")
    public List<EventDto> getUserEvents(@PathVariable Long uid,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(String.format(logTemplate + "user id: [%d] from: [%d] size: [%d]",
                "GET USER EVENTS", uid, from, size));
        return eventService.getUserEvents(uid, from, size);
    }

    @GetMapping("/{uid}/events/{eventId}")
    public EventDto getUserEventById(@PathVariable Long uid, @PathVariable Long eventId) {
        log.info(String.format(logTemplate + "user id: [%d] event id: [%d]",
                "GET BY ID", uid, eventId));
        return eventService.getUserEventById(uid, eventId);
    }

    @GetMapping("/{uid}/events/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable Long uid, @PathVariable Long eventId) {
        log.info(String.format(logTemplate + "user id: [%d] event id: [%d]",
                "GET REQUESTS", uid, eventId));
        return eventService.getRequests(uid, eventId);
    }

    @PostMapping("/{uid}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto saveEvent(@PathVariable Long uid, @Valid @RequestBody EventCreationDto event) {
        log.info(String.format(logTemplate + "user id: [%d] event: [%s]", "SAVE", uid, event));
        return eventService.saveEvent(uid, event);
    }

    @PatchMapping("/{uid}/events/{eventId}")
    public EventDto updateByUser(@PathVariable Long uid, @PathVariable Long eventId, @RequestBody UpdateEventDto event) {
        log.info(String.format(logTemplate + "user id: [%d] event id: [%d] event: [%s]",
                "UPDATE", uid, eventId, event));
        return eventService.updateEventByUser(uid, eventId, event);
    }
}
