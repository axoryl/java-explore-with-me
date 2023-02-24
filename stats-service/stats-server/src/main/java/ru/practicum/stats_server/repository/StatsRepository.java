package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats_server.model.Stats;
import ru.practicum.stats_server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query(" SELECT new ru.practicum.stats_server.model.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN (:uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(" SELECT new ru.practicum.stats_server.model.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN (:uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findByTimestampBetweenAndUriInAndUniqueIp(LocalDateTime start,
                                                              LocalDateTime end,
                                                              List<String> uris);
}
