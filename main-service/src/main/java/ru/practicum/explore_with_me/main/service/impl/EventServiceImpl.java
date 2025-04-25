package ru.practicum.explore_with_me.main.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;
import ru.practicum.explore_with_me.main.dao.converter.EventMapper;
import ru.practicum.explore_with_me.main.dao.entity.CategoryEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dao.model.Location;
import ru.practicum.explore_with_me.main.dao.repository.CategoryRepository;
import ru.practicum.explore_with_me.main.dao.repository.EventRepository;
import ru.practicum.explore_with_me.main.dao.repository.UserRepository;
import ru.practicum.explore_with_me.main.dao.specfication.EventSpecification;
import ru.practicum.explore_with_me.main.dto.event.*;
import ru.practicum.explore_with_me.main.dto.misc.EventSort;
import ru.practicum.explore_with_me.main.exception.ConflictException;
import ru.practicum.explore_with_me.main.exception.NotFoundException;
import ru.practicum.explore_with_me.main.exception.ValidationException;
import ru.practicum.explore_with_me.main.service.EventService;
import ru.practicum.explore_with_me.main.service.StatsService;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsService statsService;

    @Override
    public List<EventDto> getEventList(EventSearchParamDto searchParamDto) {
        log.debug("Get events by search param");
        if (searchParamDto.getRangeStart() != null && searchParamDto.getRangeEnd() != null &&
                searchParamDto.getRangeStart().isAfter(searchParamDto.getRangeEnd())) {
            throw new ValidationException("Start date is after end date");
        }

        Pageable pageable = PageRequest.of(searchParamDto.getFrom() / searchParamDto.getSize(), searchParamDto.getSize());
        Specification<EventEntity> specification = Specification
                .where(EventSpecification.withAnnotationOrDescriptionLike(searchParamDto.getText()))
                .and(EventSpecification.withCategoryIdIn(searchParamDto.getCategories()))
                .and(EventSpecification.withPaidEquals(searchParamDto.getPaid()))
                .and(EventSpecification
                        .withEventDateBetween(searchParamDto.getRangeStart(), searchParamDto.getRangeEnd()))
                .and(EventSpecification.withOnlyAvailable(searchParamDto.getOnlyAvailable()));

        List<EventEntity> eventEntityList = eventRepository.findAll(specification, pageable).getContent();
        if (eventEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> uris = new HashSet<>();
        for (EventEntity event : eventEntityList) {
            uris.add(MainServiceConstants.EVENT_URL_PATH + event.getId());
        }
        Map<String, Integer> hitCountStatistic = statsService.getAppUriHitCountStatistic(uris);
        Comparator<EventDto> comparator = EventSort.EVENT_DATE.equals(searchParamDto.getSort()) ?
                Comparator.comparing(EventDto::getEventDate) : Comparator.comparing(EventDto::getViews);
        return eventEntityList.stream()
                .map(event -> eventMapper.toEventDto(
                        event, MainServiceConstants.EVENT_URL_PATH, hitCountStatistic))
                .sorted(comparator)
                .toList();
    }

    @Override
    public EventDto getEventById(Long id) {
        log.debug("Get event with id = {}", id);
        EventEntity eventEntity = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + id));

        String uri = MainServiceConstants.EVENT_URL_PATH + eventEntity.getId();
        return eventMapper.toEventDto(
                eventEntity,
                MainServiceConstants.EVENT_URL_PATH,
                statsService.getUniqueAppUriHitCountStatistic(Set.of(uri))
        );
    }

    @Override
    public List<EventDto> getPrivateEventList(Long userId, int from, int size) {
        log.debug("Get private events with user id = {} and from = {}, size = {}", userId, from, size);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id = " + userId);
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<EventEntity> eventEntityList = eventRepository.findAllByInitiator_Id(userId, pageable);
        Set<String> uris = new HashSet<>();
        for (EventEntity event : eventEntityList) {
            uris.add(MainServiceConstants.EVENT_URL_PATH + event.getId());
        }
        Map<String, Integer> hitCountStatistic = statsService.getAppUriHitCountStatistic(uris);
        return eventEntityList.stream()
                .map(event -> eventMapper.toEventDto(
                        event, MainServiceConstants.EVENT_URL_PATH, hitCountStatistic))
                .toList();
    }

    @Override
    @Transactional
    public EventDto createPrivateEvent(Long userId, EventPrivateCreateRequestDto dto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        CategoryEntity categoryEntity = categoryRepository.findById(dto.getCategory()).orElseThrow(
                () -> new NotFoundException("Category not found with id = " + dto.getCategory()));

        EventEntity eventEntity = eventMapper.toEventEntity(dto, userEntity, categoryEntity);
        eventRepository.save(eventEntity);
        log.debug("Private event was created with id = {}", eventEntity.getId());
        return eventMapper.toEventDto(eventEntity);
    }

    @Override
    public EventDto getPrivateEventById(Long id, Long userId) {
        log.debug("Get private event with id = {} and user id = {}", id, userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id = " + userId);
        }
        EventEntity eventEntity = eventRepository.findByIdAndInitiator_Id(id, userId)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + id));

        String uri = MainServiceConstants.EVENT_URL_PATH + eventEntity.getId();
        return eventMapper.toEventDto(
                eventEntity,
                MainServiceConstants.EVENT_URL_PATH,
                statsService.getUniqueAppUriHitCountStatistic(Set.of(uri))
        );
    }

    @Override
    @Transactional
    public EventDto updatePrivateEvent(Long id, Long userId, EventUpdateRequestDto dto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id = " + userId);
        }
        EventEntity eventEntity = eventRepository.findByIdAndInitiator_Id(id, userId)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + id));
        if (EventState.PUBLISHED.equals(eventEntity.getState())) {
            throw new ConflictException("Event already published");
        }
        toUpdatedEventEntity(eventEntity, dto);

        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Event date should be before 2 hour of publish date");
            }
            eventEntity.setEventDate(dto.getEventDate());
        }

        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == EventState.CANCEL_REVIEW) {
                eventEntity.setState(EventState.CANCELED);
                eventEntity.setPublished(null);
            } else if (dto.getStateAction() == EventState.SEND_TO_REVIEW) {
                eventEntity.setState(EventState.PENDING);
                eventEntity.setPublished(null);
            }
        }

        log.debug("Private event request with id = {} was updated", id);
        String uri = MainServiceConstants.EVENT_URL_PATH + eventEntity.getId();
        return eventMapper.toEventDto(
                eventEntity,
                MainServiceConstants.EVENT_URL_PATH,
                statsService.getAppUriHitCountStatistic(Set.of(uri))
        );
    }

    @Override
    public List<EventDto> getAdminEventList(EventAdminSearchParamDto searchParamDto) {
        log.debug("Get admin event list by search param");
        Pageable pageable = PageRequest.of(searchParamDto.getFrom() / searchParamDto.getSize(), searchParamDto.getSize());
        Specification<EventEntity> specification = Specification
                .where(EventSpecification.withInitiatorIdIn(searchParamDto.getUsers()))
                .and(EventSpecification.withStateIn(searchParamDto.getStates()))
                .and(EventSpecification.withCategoryIdIn(searchParamDto.getCategories()))
                .and(EventSpecification
                        .withEventDateBetween(searchParamDto.getRangeStart(), searchParamDto.getRangeEnd()));

        List<EventEntity> eventEntityList = eventRepository.findAll(specification, pageable).getContent();
        if (eventEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> uris = new HashSet<>();
        for (EventEntity event : eventEntityList) {
            uris.add(MainServiceConstants.EVENT_URL_PATH + event.getId());
        }
        Map<String, Integer> hitCountStatistic = statsService.getAppUriHitCountStatistic(uris);
        return eventEntityList.stream()
                .map(event -> eventMapper.toEventDto(
                        event, MainServiceConstants.EVENT_URL_PATH, hitCountStatistic))
                .toList();
    }

    @Override
    @Transactional
    public EventDto updateAdminEvent(Long id, EventUpdateRequestDto dto) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + id));
        if (eventEntity.getState() == EventState.PUBLISHED || eventEntity.getState() == EventState.CANCELED) {
            throw new ConflictException("Event already published or canceled");
        }
        toUpdatedEventEntity(eventEntity, dto);
        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ValidationException("Event date should be before 1 hour of publish date");
            }
            eventEntity.setEventDate(dto.getEventDate());
        }

        if (dto.getStateAction() == EventState.PUBLISH_EVENT) {
            eventEntity.setState(EventState.PUBLISHED);
            eventEntity.setPublished(LocalDateTime.now());
        } else if (dto.getStateAction() == EventState.REJECT_EVENT) {
            eventEntity.setState(EventState.CANCELED);
            eventEntity.setPublished(null);
        }

        log.debug("Event with id = {} was updated by admin", id);
        String uri = MainServiceConstants.EVENT_URL_PATH + eventEntity.getId();
        return eventMapper.toEventDto(
                eventEntity,
                MainServiceConstants.EVENT_URL_PATH,
                statsService.getAppUriHitCountStatistic(Set.of(uri))
        );
    }

    private void toUpdatedEventEntity(EventEntity eventEntity, EventUpdateRequestDto dto) {
        if (dto.getAnnotation() != null) {
            eventEntity.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            eventEntity.setDescription(dto.getDescription());
        }
        if (dto.getTitle() != null) {
            eventEntity.setTitle(dto.getTitle());
        }
        if (dto.getCategory() != null && dto.getCategory() > 0) {
            eventEntity.setCategory(categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found with id = " + dto.getCategory())));
        }
        if (dto.getPaid() != null) {
            eventEntity.setPaid(dto.getPaid());
        }
        if (dto.getRequestModeration() != null) {
            eventEntity.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getParticipantLimit() != null) {
            eventEntity.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getLocation() != null) {
            Location location = new Location();
            location.setLat(dto.getLocation().getLat());
            location.setLon(dto.getLocation().getLon());

            eventEntity.setLocation(location);
        }
    }
}
