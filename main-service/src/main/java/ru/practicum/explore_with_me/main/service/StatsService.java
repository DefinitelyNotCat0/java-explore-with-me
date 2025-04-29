package ru.practicum.explore_with_me.main.service;

import java.util.Map;
import java.util.Set;

public interface StatsService {

    Map<String, Integer> getAppUriHitCountStatistic(Set<String> uris);

    Map<String, Integer> getUniqueAppUriHitCountStatistic(Set<String> uris);

    void addHit(String uri, String ip);
}
