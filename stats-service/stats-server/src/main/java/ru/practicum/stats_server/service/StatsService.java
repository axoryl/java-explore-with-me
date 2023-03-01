package ru.practicum.stats_server.service;

import ru.practicum.dto.StatsCreationDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsShortDto;

import java.util.List;

public interface StatsService {

    List<StatsShortDto> getStats(String start, String end, List<String> uris, boolean unique);

    StatsDto addHit(StatsCreationDto statsDto);
}
