package ru.practicum.explore_with_me.main.service;

import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestsPrivateUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestsPrivateUpdateResultDto;

import java.util.List;

public interface EventRequestService {

    List<EventRequestDto> getPrivateEventRequestListByEventId(Long eventId, Long userId);

    EventRequestsPrivateUpdateResultDto updatePrivateEventRequests(Long eventId,
                                                                   Long userId,
                                                                   EventRequestsPrivateUpdateRequestDto dto);

    List<EventRequestDto> getPrivateEventRequestList(Long userId);

    EventRequestDto createPrivateEventRequest(Long eventId, Long userId);

    EventRequestDto cancelPrivateEventRequest(Long requestId, Long userId);
}
