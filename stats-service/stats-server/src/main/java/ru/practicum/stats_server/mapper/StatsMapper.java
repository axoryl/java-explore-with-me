package ru.practicum.stats_server.mapper;

import ru.practicum.dto.StatsCreationDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsShortDto;
import ru.practicum.stats_server.model.Stats;
import ru.practicum.stats_server.model.ViewStats;

public class StatsMapper {

    private StatsMapper() {

    }

    public static Stats mapToStats(final StatsCreationDto statsDto) {
        return Stats.builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .ip(statsDto.getIp())
                .timestamp(statsDto.getTimestamp())
                .build();
    }

    public static StatsDto mapToStatsDto(final Stats stats) {
        return StatsDto.builder()
                .id(stats.getId())
                .app(stats.getApp())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp())
                .build();
    }

    public static StatsShortDto mapToStatsShortDto(final ViewStats stats) {
        return StatsShortDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }
}
