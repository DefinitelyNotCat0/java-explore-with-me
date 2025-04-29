package ru.practicum.explore_with_me.stat.server.service.impl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.practicum.explore_with_me.stat.server.dao.converter.AppUriHitCountMapper;
import ru.practicum.explore_with_me.stat.server.dao.converter.HitMapper;
import ru.practicum.explore_with_me.stat.server.dao.entity.HitEntity;
import ru.practicum.explore_with_me.stat.server.dao.model.AppUriHitCountEntity;
import ru.practicum.explore_with_me.stat.server.dao.repository.HitRepository;
import ru.practicum.explore_with_me.stat.server.service.StatService;
import ru.practicum.explore_with_me.stats.dto.AppUriHitCountDto;
import ru.practicum.explore_with_me.stats.dto.HitAddRequestDto;
import ru.practicum.explore_with_me.stats.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final HitRepository hitRepository;
    private final HitMapper hitMapper;
    private final AppUriHitCountMapper appUriHitCountMapper;

    @Override
    @Transactional
    public HitDto addHit(HitAddRequestDto hitDto) {
        HitEntity hitEntity = hitMapper.toHitEntity(hitDto);
        hitRepository.save(hitEntity);
        log.debug("Hit was created with id = {}", hitEntity.getId());
        return hitMapper.toHitDto(hitEntity);
    }

    @Override
    public List<AppUriHitCountDto> getAppUriHitCountStatistic(LocalDateTime start,
                                                              LocalDateTime end,
                                                              List<String> uriList,
                                                              boolean isUnique) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start date is after end date");
        }

        List<AppUriHitCountEntity> appUriHitCountEntityList;
        if (ObjectUtils.isEmpty(uriList)) {
            if (isUnique) {
                appUriHitCountEntityList = hitRepository.getStatsByDatesUnique(start, end);
            } else {
                appUriHitCountEntityList = hitRepository.getStatsByDates(start, end);
            }
        } else {
            if (isUnique) {
                appUriHitCountEntityList = hitRepository.getStatsByDatesAndUriUnique(start, end, uriList);
            } else {
                appUriHitCountEntityList = hitRepository.getStatsByDatesAndUri(start, end, uriList);
            }
        }

        log.debug("Get app uri hit count statistics with start date = {}," +
                        "end date = {}, uriList count = {} and unique = {}",
                start, end, uriList != null ? uriList.size() : 0, isUnique);
        return appUriHitCountEntityList
                .stream()
                .map(appUriHitCountMapper::toAppUriHitCountDto)
                .toList();
    }
}
