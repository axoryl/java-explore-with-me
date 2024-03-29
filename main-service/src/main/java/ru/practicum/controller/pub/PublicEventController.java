package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.dto.StatsCreationDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.service.EventService;
import ru.practicum.util.StringTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicEventController {

    private final EventService eventService;
    private final StatsClient statsClient;
    private static final String logTemplate = StringTemplate.PUBLIC_EVENT_LOG;

    @GetMapping
    public List<EventDto> getAllEvents(@RequestParam(required = false, defaultValue = "") String text,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) boolean paid,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(required = false) boolean onlyAvailable,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                       HttpServletRequest httpServletRequest) {

        statsClient.addHit(StatsCreationDto.builder()
                .app("ewm-main")
                .ip(httpServletRequest.getRemoteAddr())
                .uri(httpServletRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
        log.info(String.format(logTemplate, "GET ALL"));
        return eventService.getAllEventsForUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        statsClient.addHit(StatsCreationDto.builder()
                .app("ewm-main")
                .ip(httpServletRequest.getRemoteAddr())
                .uri(httpServletRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
        log.info(String.format(logTemplate, "GET BY ID"));
        return eventService.getEventById(eventId);
    }
}
