package ru.practicum.explore_with_me.main.service;

import ru.practicum.explore_with_me.main.dto.event.*;

import java.util.List;

public interface EventService {

    List<EventDto> getEventList(EventSearchParamDto searchParamDto);

    EventDto getEventById(Long id);

    List<EventDto> getPrivateEventList(Long userId, int from, int size);

    EventDto createPrivateEvent(Long userId, EventPrivateCreateRequestDto dto);

    EventDto getPrivateEventById(Long id, Long userId);

    EventDto updatePrivateEvent(Long id, Long userId, EventUpdateRequestDto dto);

    List<EventDto> getAdminEventList(EventAdminSearchParamDto searchParamDto);

    EventDto updateAdminEvent(Long id, EventUpdateRequestDto dto);
}
