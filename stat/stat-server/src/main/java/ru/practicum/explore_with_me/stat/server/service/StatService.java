package ru.practicum.explore_with_me.stat.server.service;


import ru.practicum.explore_with_me.stats.dto.AppUriHitCountDto;
import ru.practicum.explore_with_me.stats.dto.HitAddRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    void add(HitAddRequestDto hitDto);

    List<AppUriHitCountDto> getAppUriHitCountStatistic(LocalDateTime start,
                                                       LocalDateTime end,
                                                       List<String> uriList,
                                                       boolean isUnique);
}
