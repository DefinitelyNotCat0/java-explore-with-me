package ru.practicum.explore_with_me.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;
import ru.practicum.explore_with_me.main.service.StatsService;
import ru.practicum.explore_with_me.stat.client.StatClient;
import ru.practicum.explore_with_me.stats.dto.AppUriHitCountDto;
import ru.practicum.explore_with_me.stats.dto.HitAddRequestDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
@ComponentScan(basePackages = "ru.practicum")
public class StatsServiceImpl implements StatsService {

    private final String appName;

    private final StatClient statClient;

    StatsServiceImpl(StatClient statClient,
                     @Value("${app.name}") String appName) {
        this.statClient = statClient;
        this.appName = appName;
    }

    @Override
    public Map<String, Integer> getAppUriHitCountStatistic(Set<String> uris) {
        log.debug("Get app uri hit count statistic");
        return getHitCountStatistic(uris, false);
    }

    @Override
    public Map<String, Integer> getUniqueAppUriHitCountStatistic(Set<String> uris) {
        log.debug("Get unique app uri hit count statistic");
        return getHitCountStatistic(uris, true);
    }

    @Override
    public void addHit(String uri, String ip) {
        HitAddRequestDto request = new HitAddRequestDto();
        request.setUri(uri);
        request.setApp(appName);
        request.setIp(ip);
        request.setTimestamp(LocalDateTime.now());

        statClient.addHit(request);
        log.debug("New hit was added");
    }

    private Map<String, Integer> getHitCountStatistic(Set<String> uris, boolean unique) {
        log.debug("Get app uri hit count statistic with uris = {}", uris);
        List<AppUriHitCountDto> hits = statClient.getAppUriHitCountStatistic(
                MainServiceConstants.DATE_TIME_MIN,
                LocalDateTime.now(),
                uris,
                unique);
        Map<String, Integer> hitMap = new HashMap<>();

        for (AppUriHitCountDto hitStat : hits) {
            hitMap.put(hitStat.getUri(), hitStat.getHits());
        }

        return hitMap;
    }
}
