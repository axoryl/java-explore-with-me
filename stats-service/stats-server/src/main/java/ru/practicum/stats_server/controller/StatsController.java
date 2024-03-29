package ru.practicum.stats_server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatsCreationDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsShortDto;
import ru.practicum.stats_server.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;

    @GetMapping("/stats")
    public List<StatsShortDto> getStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam List<String> uris,
                                        @RequestParam(required = false) boolean unique) {
        log.info(String.format(">>> STATS CONTROLLER GET STATS --> start: [%s] end: [%s] uris: [%s] unique: [%b]",
                start, end, uris, unique));
        return service.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsDto addHit(@RequestBody StatsCreationDto statsDto) {
        log.info(String.format(">>> STATS CONTROLLER HIT --> requestBody: [%s]", statsDto));
        return service.addHit(statsDto);
    }
}
