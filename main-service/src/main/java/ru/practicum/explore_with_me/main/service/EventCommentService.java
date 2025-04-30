package ru.practicum.explore_with_me.main.service;

import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentDto;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentPrivateCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentPrivateUpdateRequestDto;

import java.util.List;

public interface EventCommentService {

    List<EventCommentDto> getEventCommentList(Long eventId);

    EventCommentDto createPrivateEventComment(Long eventId,
                                              Long userId,
                                              EventCommentPrivateCreateRequestDto dto);

    EventCommentDto updatePrivateEventComment(Long commentId,
                                              Long userId,
                                              EventCommentPrivateUpdateRequestDto dto);

    void deletePrivateEventComment(Long commentId, Long userId);
}
