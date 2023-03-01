package ru.practicum;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.practicum.dto.StatsCreationDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsShortDto;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StatsClient {

    public static final String BASE_URL = "http://localhost:9090";
    private final WebClient client;

    public StatsClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .responseTimeout(Duration.ofMillis(1000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS)));

        client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public StatsDto addHit(final StatsCreationDto statsCreationDto) {
        return client.post()
                .uri("/hit")
                .bodyValue(statsCreationDto)
                .retrieve()
                .bodyToMono(StatsDto.class)
                .block();
    }

    public List<StatsShortDto> getViewStats(final String start,
                                            final String end,
                                            final List<String> uris,
                                            final Boolean unique) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StatsShortDto>>() {
                })
                .block();
    }
}
