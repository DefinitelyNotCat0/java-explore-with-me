package ru.practicum.explore_with_me.main.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.main.dao.converter.EventCommentMapper;
import ru.practicum.explore_with_me.main.dao.entity.EventCommentEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dao.repository.EventCommentRepository;
import ru.practicum.explore_with_me.main.dao.repository.EventRepository;
import ru.practicum.explore_with_me.main.dao.repository.UserRepository;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentDto;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentPrivateCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentPrivateUpdateRequestDto;
import ru.practicum.explore_with_me.main.exception.NotFoundException;
import ru.practicum.explore_with_me.main.exception.ValidationException;
import ru.practicum.explore_with_me.main.service.EventCommentService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventCommentServiceImpl implements EventCommentService {

    private final EventCommentRepository eventCommentRepository;
    private final EventCommentMapper eventCommentMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventCommentDto> getEventCommentList(Long eventId) {
        log.debug("Get event comments with event id = " + eventId);
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + eventId));
        return eventCommentRepository.findAllByEventId(eventEntity.getId())
                .stream()
                .map(eventCommentMapper::toEventCommentDto)
                .toList();
    }

    @Override
    @Transactional
    public EventCommentDto createPrivateEventComment(Long eventId,
                                                     Long userId,
                                                     EventCommentPrivateCreateRequestDto dto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id = " + eventId));
        EventCommentEntity eventCommentEntity = eventCommentMapper.toEventCommentEntity(dto, userEntity, eventEntity);
        eventCommentRepository.save(eventCommentEntity);
        log.debug("Event comment was created with id = {}", eventCommentEntity.getId());
        return eventCommentMapper.toEventCommentDto(eventCommentEntity);
    }

    @Override
    public EventCommentDto updatePrivateEventComment(Long commentId,
                                                     Long userId,
                                                     EventCommentPrivateUpdateRequestDto dto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id = " + userId);
        }
        EventCommentEntity eventCommentEntity = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Event comment not found with id = " + commentId));
        if (!eventCommentEntity.getAuthor().getId().equals(userId)) {
            throw new ValidationException("Only author of the comment can update it");
        }

        eventCommentEntity.setMessage(dto.getMessage());
        eventCommentEntity.setLastUpdateDay(LocalDateTime.now());
        log.debug("Event comment was updated with id = {}", commentId);
        return eventCommentMapper.toEventCommentDto(eventCommentEntity);
    }

    @Override
    @Transactional
    public void deletePrivateEventComment(Long commentId, Long userId) {
        EventCommentEntity eventCommentEntity = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Event comment not found with id = " + commentId));
        if (!eventCommentEntity.getAuthor().getId().equals(userId)) {
            throw new ValidationException("Only author can delete comment");
        }
        eventCommentRepository.deleteById(commentId);
        log.debug("Comment was deleted with id = {}", commentId);
    }
}
