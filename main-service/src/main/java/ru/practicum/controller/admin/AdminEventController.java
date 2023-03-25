package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.UpdateEventDto;
import ru.practicum.service.EventService;
import ru.practicum.util.StringTemplate;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminEventController {

    private final EventService eventService;
    private static final String logTemplate = StringTemplate.ADMIN_EVENT_LOG;

    @GetMapping
    public List<EventDto> getAllEvents(@RequestParam(required = false) List<Long> users,
                                       @RequestParam(required = false) List<String> states,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(String.format(logTemplate, "GET ALL EVENTS"));
        return eventService.getAllEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long eventId, @RequestBody UpdateEventDto updatedEvent) {
        log.info(String.format(logTemplate, "UPDATE"));
        return eventService.updateEventByAdmin(eventId, updatedEvent);
    }
}
