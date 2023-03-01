package ru.practicum.stats_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatsCreationDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsShortDto;
import ru.practicum.stats_server.mapper.StatsMapper;
import ru.practicum.stats_server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.stats_server.mapper.StatsMapper.mapToStats;
import static ru.practicum.stats_server.mapper.StatsMapper.mapToStatsDto;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public List<StatsShortDto> getStats(final String start, final String end,
                                        final List<String> uris, final boolean isUnique) {
        final var timeStart = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        final var timeEnd = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));

        if (isUnique) {
            return statsRepository.findByTimestampBetweenAndUriInAndUniqueIp(timeStart, timeEnd, uris).stream()
                    .map(StatsMapper::mapToStatsShortDto)
                    .collect(Collectors.toList());
        }
        return statsRepository.findByTimestampBetweenAndUriIn(timeStart, timeEnd, uris).stream()
                .map(StatsMapper::mapToStatsShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public StatsDto addHit(final StatsCreationDto statsDto) {
        return mapToStatsDto(statsRepository.save(mapToStats(statsDto)));
    }
}
