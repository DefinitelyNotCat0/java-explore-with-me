package ru.practicum.explore_with_me.main.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.practicum.explore_with_me.main.dao.converter.EventRequestMapper;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventRequestEntity;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dao.repository.EventRepository;
import ru.practicum.explore_with_me.main.dao.repository.EventRequestRepository;
import ru.practicum.explore_with_me.main.dao.repository.UserRepository;
import ru.practicum.explore_with_me.main.dto.event.EventState;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestStatus;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestsPrivateUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestsPrivateUpdateResultDto;
import ru.practicum.explore_with_me.main.exception.ConflictException;
import ru.practicum.explore_with_me.main.exception.NotFoundException;
import ru.practicum.explore_with_me.main.exception.ValidationException;
import ru.practicum.explore_with_me.main.service.EventRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService {

    private final EventRequestRepository eventRequestRepository;
    private final EventRequestMapper eventRequestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventRequestDto> getPrivateEventRequestListByEventId(Long eventId, Long userId) {
        log.debug("Get private event requests by event id = {} and user id = {}", eventId, userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id = " + userId);
        }
        if (!eventRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new NotFoundException("Event with id = " + eventId + " and initiator = " + userId + " not found");
        }

        return eventRequestRepository.findAllByEvent_Id(eventId)
                .stream()
                .map(eventRequestMapper::toEventRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public EventRequestsPrivateUpdateResultDto updatePrivateEventRequests(Long eventId,
                                                                          Long userId,
                                                                          EventRequestsPrivateUpdateRequestDto dto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id = " + userId);
        }
        EventEntity eventEntity = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId +
                        " and initiator = " + userId + " not found"));

        if (!eventEntity.isRequestModeration()) {
            throw new ValidationException("Moderation not required");
        }
        if (eventEntity.getParticipantLimit() == 0) {
            throw new ValidationException("Request list is not limited");
        }

        Map<EventRequestStatus, List<EventRequestEntity>> eventRequestStatusMap =
                mapEventRequestsByStatus(eventEntity, dto, eventRequestRepository.findAllByEvent_Id(eventId));
        List<EventRequestEntity> confirmedEventRequests = eventRequestStatusMap.get(EventRequestStatus.CONFIRMED);
        List<EventRequestEntity> rejectedEventRequests = eventRequestStatusMap.get(EventRequestStatus.REJECTED);
        List<EventRequestEntity> pendingEventRequests = eventRequestStatusMap.get(EventRequestStatus.PENDING);
        eventRequestRepository.saveAll(confirmedEventRequests);
        eventRequestRepository.saveAll(rejectedEventRequests);
        if (!ObjectUtils.isEmpty(pendingEventRequests)) {
            eventRequestRepository.saveAll(
                    pendingEventRequests
                            .stream()
                            .peek(eventRequest ->
                                    eventRequest.setStatus(EventRequestStatus.REJECTED)).toList()
            );
        }

        eventEntity.setConfirmedRequests(eventRequestRepository
                .countByEvent_IdAndStatus(eventId, EventRequestStatus.CONFIRMED));
        eventRepository.save(eventEntity);
        log.debug("Private event requests was updated");

        return new EventRequestsPrivateUpdateResultDto(
                confirmedEventRequests.stream().map(eventRequestMapper::toEventRequestDto).toList(),
                rejectedEventRequests.stream().map(eventRequestMapper::toEventRequestDto).toList()
        );
    }

    @Override
    public List<EventRequestDto> getPrivateEventRequestList(Long userId) {
        log.debug("Get private event requests by user id = {}", userId);
        return eventRequestRepository.findAllByRequester_Id(userId)
                .stream()
                .map(eventRequestMapper::toEventRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public EventRequestDto createPrivateEventRequest(Long eventId, Long userId) {
        if (eventRequestRepository.existsByEvent_IdAndRequester_Id(eventId, userId)) {
            throw new ConflictException("Event request already exists");
        }

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + eventId));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        if (eventEntity.getInitiator().getId().equals(userEntity.getId())) {
            throw new ConflictException("Event initiator and event requester is the same person");
        }
        if (!EventState.PUBLISHED.equals(eventEntity.getState())) {
            throw new ConflictException("Event is not published yet");
        }
        if (eventEntity.getParticipantLimit() > 0 &&
                eventEntity.getParticipantLimit() <= eventEntity.getConfirmedRequests()) {
            throw new ConflictException("Event has no free places");
        }

        EventRequestEntity eventRequestEntity = new EventRequestEntity();
        eventRequestMapper.enrichEventRequestEntity(eventRequestEntity, eventEntity, userEntity);
        eventRequestRepository.save(eventRequestEntity);
        log.debug("Event request was created with id = {}", eventRequestEntity.getId());
        if (!eventEntity.isRequestModeration()) {
            updateEventConfirmedRequests(eventRequestEntity.getEvent().getId());
        }
        return eventRequestMapper.toEventRequestDto(eventRequestEntity);
    }

    @Override
    @Transactional
    public EventRequestDto cancelPrivateEventRequest(Long requestId, Long userId) {
        EventRequestEntity eventRequestEntity = eventRequestRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Event request  not found with id = " + requestId));
        eventRequestEntity.setStatus(EventRequestStatus.CANCELED);

        if (EventRequestStatus.CONFIRMED.equals(eventRequestEntity.getStatus())) {
            updateEventConfirmedRequests(eventRequestEntity.getEvent().getId());
        }
        log.debug("Private event request with id = {} was canceled by user id = {}", requestId, userId);
        return eventRequestMapper.toEventRequestDto(eventRequestEntity);
    }

    private Map<EventRequestStatus, List<EventRequestEntity>>
    mapEventRequestsByStatus(EventEntity eventEntity,
                             EventRequestsPrivateUpdateRequestDto dto,
                             List<EventRequestEntity> eventRequestList) {
        List<EventRequestEntity> confirmedEventRequests = new ArrayList<>();
        List<EventRequestEntity> rejectedEventRequests = new ArrayList<>();
        List<EventRequestEntity> pendingEventRequests = new ArrayList<>();

        int limit = eventEntity.getParticipantLimit();
        int current = eventEntity.getConfirmedRequests();
        if (limit <= current) {
            throw new ConflictException("Event has no free spaces");
        }

        for (EventRequestEntity eventRequest : eventRequestList) {
            if (!dto.getRequestIds().contains(eventRequest.getId())) {
                if (EventRequestStatus.PENDING.equals(eventRequest.getStatus())) {
                    pendingEventRequests.add(eventRequest);
                }
                continue;
            }

            if (EventRequestStatus.CONFIRMED.equals(dto.getStatus()) && current < limit) {
                eventRequest.setStatus(EventRequestStatus.CONFIRMED);
                confirmedEventRequests.add(eventRequest);
                current++;
                continue;
            }
            eventRequest.setStatus(EventRequestStatus.REJECTED);
            rejectedEventRequests.add(eventRequest);
        }

        if (current >= limit) {
            pendingEventRequests = List.of();
        }
        return Map.of(
                EventRequestStatus.CONFIRMED, confirmedEventRequests,
                EventRequestStatus.REJECTED, rejectedEventRequests,
                EventRequestStatus.PENDING, pendingEventRequests
        );
    }

    private void updateEventConfirmedRequests(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + eventId));
        eventEntity.setConfirmedRequests(eventRequestRepository
                .countByEvent_IdAndStatus(eventId, EventRequestStatus.CONFIRMED));
        eventRepository.save(eventEntity);
    }
}
