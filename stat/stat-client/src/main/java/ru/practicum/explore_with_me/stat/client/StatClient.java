package ru.practicum.explore_with_me.stat.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.explore_with_me.stats.dto.AppUriHitCountDto;
import ru.practicum.explore_with_me.stats.dto.HitAddRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class StatClient {

    private static final String API_HIT = "/hit";
    private static final String API_STATS = "/stats";

    private final WebClient webClient;

    StatClient(@Value("${stat.server.url}") String serverUri) {
        this.webClient = WebClient.builder()
                .baseUrl(serverUri)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void addHit(HitAddRequestDto dto) {
        webClient.post()
                .uri(API_HIT)
                .bodyValue(dto)
                .retrieve()
                .toBodilessEntity()
                .onErrorComplete()
                .block();
    }

    public List<AppUriHitCountDto> getAppUriHitCountStatistic(LocalDateTime start,
                                                              LocalDateTime end,
                                                              Set<String> uriList,
                                                              boolean isUnique) {
        Optional<String> urisOptional = ObjectUtils.isEmpty(uriList) ?
                Optional.empty() :
                Optional.of(String.join(",", uriList));


        List<AppUriHitCountDto> response = webClient.get()
                .uri(builder -> builder.path(API_STATS)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("unique", isUnique)
                        .queryParamIfPresent("uris", urisOptional)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AppUriHitCountDto>>() {
                })
                .onErrorReturn(Collections.emptyList())
                .block();

        return response == null ? Collections.emptyList() : response;
    }
}
