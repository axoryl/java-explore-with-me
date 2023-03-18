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

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping("/{uid}/events")
    public List<EventDto> getUserEvents(@PathVariable Long uid,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(">>> PRIVATE EVENT GET USER EVENTS --> user id: [" + uid + "]" +
                " from: [" + from + "] size: [" + size + "]");
        return eventService.getUserEvents(uid, from, size);
    }

    @GetMapping("/{uid}/events/{eventId}")
    public EventDto getUserEventById(@PathVariable Long uid, @PathVariable Long eventId) {
        log.info(">>> PRIVATE EVENT GET BY ID --> user id: [" + uid + "] event id: [" + eventId + "]");
        return eventService.getUserEventById(uid, eventId);
    }

    @GetMapping("/{uid}/events/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable Long uid, @PathVariable Long eventId) {
        log.info(">>> PRIVATE EVENT GET REQUESTS --> user id: [" + uid + "] event id: [" + eventId + "]");
        return eventService.getRequests(uid, eventId);
    }

    @PostMapping("/{uid}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto saveEvent(@PathVariable Long uid, @Valid @RequestBody EventCreationDto event) {
        log.info(">>> PRIVATE EVENT SAVE --> user id: [" + uid + "] event: [" + event + "]");
        return eventService.saveEvent(uid, event);
    }

    @PatchMapping("/{uid}/events/{eventId}")
    public EventDto updateByUser(@PathVariable Long uid, @PathVariable Long eventId, @RequestBody UpdateEventDto event) {
        log.info(">>> PRIVATE EVENT UPDATE --> user id: [" + uid + "] event id: [" + eventId + "]" +
                " event: [" + event + "]");
        return eventService.updateEventByUser(uid, eventId, event);
    }
}
