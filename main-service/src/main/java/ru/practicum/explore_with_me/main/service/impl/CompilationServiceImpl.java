package ru.practicum.explore_with_me.main.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;
import ru.practicum.explore_with_me.main.dao.converter.CompilationMapper;
import ru.practicum.explore_with_me.main.dao.entity.CompilationEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dao.repository.CompilationRepository;
import ru.practicum.explore_with_me.main.dao.repository.EventRepository;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.main.exception.NotFoundException;
import ru.practicum.explore_with_me.main.service.CompilationService;
import ru.practicum.explore_with_me.main.service.StatsService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final StatsService statsService;

    @Override
    public List<CompilationDto> getCompilationList(boolean pinned, int from, int size) {
        log.debug("Get compilation list with from = {} and size = {}", from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        List<CompilationEntity> compilationEntityList = pinned ? compilationRepository.findAllByPinned(true, pageable) :
                compilationRepository.findAll(pageable).getContent();

        Map<Long, Integer> compilationViewsMap = getCompilationViewsMap(compilationEntityList);
        return compilationEntityList
                .stream()
                .map(compilationEntity ->
                        compilationMapper.toCompilationDto(compilationEntity, compilationViewsMap))
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(Long id) {
        log.debug("Get compilation with id = {}", id);
        CompilationEntity compilationEntity = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found with id = " + id));

        Map<Long, Integer> compilationViewsMap = getCompilationViewsMap(Collections.singletonList(compilationEntity));
        return compilationMapper.toCompilationDto(compilationEntity, compilationViewsMap);
    }

    @Override
    @Transactional
    public CompilationDto createAdminCompilation(CompilationAdminCreateRequestDto dto) {
        Set<EventEntity> eventEntitySet = new HashSet<>();
        if (!ObjectUtils.isEmpty(dto.getEvents())) {
            eventEntitySet = eventRepository.findAllByIdIn(dto.getEvents());
            if (ObjectUtils.isEmpty(eventEntitySet)) {
                throw new NotFoundException("Events not found");
            }
        }

        CompilationEntity compilationEntity = compilationMapper.toCompilationEntity(dto, eventEntitySet);
        compilationRepository.save(compilationEntity);
        log.debug("Compilation created with id = {} by admin", compilationEntity.getId());

        Map<Long, Integer> compilationViewsMap = getCompilationViewsMap(Collections.singletonList(compilationEntity));
        return compilationMapper.toCompilationDto(compilationEntity, compilationViewsMap);
    }

    @Override
    @Transactional
    public void deleteAdminCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Compilation not found with id = " + id);
        }
        compilationRepository.deleteById(id);
        log.debug("Compilation with id = {} was deleted by admin", id);
    }

    @Override
    @Transactional
    public CompilationDto updateAdminCompilation(Long id, CompilationAdminUpdateRequestDto dto) {
        CompilationEntity compilationEntity = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found with id = " + id));

        Set<EventEntity> eventEntitySet = new HashSet<>();
        if (!ObjectUtils.isEmpty(dto.getEvents())) {
            eventEntitySet = eventRepository.findAllByIdIn(dto.getEvents());
            if (ObjectUtils.isEmpty(eventEntitySet)) {
                throw new NotFoundException("Events not found");
            }
        }
        if (dto.getTitle() != null) {
            compilationEntity.setName(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            compilationEntity.setPinned(dto.getPinned());
        }
        compilationEntity.setEvents(eventEntitySet);
        log.debug("Compilation with id = {} was updated by admin", id);

        Map<Long, Integer> compilationViewsMap = getCompilationViewsMap(Collections.singletonList(compilationEntity));
        return compilationMapper.toCompilationDto(compilationEntity, compilationViewsMap);
    }

    private Map<Long, Integer> getCompilationViewsMap(List<CompilationEntity> compilationEntityList) {
        if (ObjectUtils.isEmpty(compilationEntityList)) {
            return Collections.emptyMap();
        }
        Set<String> uris = new HashSet<>();
        for (CompilationEntity compilationEntity : compilationEntityList) {
            for (EventEntity eventEntity : compilationEntity.getEvents()) {
                uris.add(MainServiceConstants.EVENT_URL_PATH + eventEntity.getId());
            }
        }
        if (uris.isEmpty()) {
            return Collections.emptyMap();
        }
        return statsService.getAppUriHitCountStatistic(uris)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e ->
                                Long.valueOf(e.getKey()
                                        .replace(MainServiceConstants.EVENT_URL_PATH, "")),
                        Map.Entry::getValue)
                );
    }
}
