package ru.practicum.explore_with_me.main.controller.event;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.main.dto.event.EventDto;
import ru.practicum.explore_with_me.main.dto.event.EventPrivateCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.event.EventUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestsPrivateUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestsPrivateUpdateResultDto;
import ru.practicum.explore_with_me.main.service.EventRequestService;
import ru.practicum.explore_with_me.main.service.EventService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;
    private final EventRequestService eventRequestService;

    @GetMapping
    public List<EventDto> getPrivateEventList(@PathVariable @Positive Long userId,
                                              @RequestParam(value = "from", defaultValue = "0")
                                              @PositiveOrZero int from,
                                              @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        return eventService.getPrivateEventList(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createPrivateEvent(@PathVariable @Positive Long userId,
                                       @RequestBody @Valid EventPrivateCreateRequestDto dto) {
        return eventService.createPrivateEvent(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventDto getPrivateEventById(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long eventId) {
        return eventService.getPrivateEventById(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventDto updatePrivateEvent(@PathVariable @Positive Long userId,
                                       @PathVariable @Positive Long eventId,
                                       @RequestBody @Valid EventUpdateRequestDto dto) {
        return eventService.updatePrivateEvent(eventId, userId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<EventRequestDto> getPrivateEventRequestListByEventId(@PathVariable @Positive Long userId,
                                                                     @PathVariable @Positive Long eventId) {
        return eventRequestService.getPrivateEventRequestListByEventId(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestsPrivateUpdateResultDto updatePrivateEventRequests(@PathVariable @Positive Long userId,
                                                                          @PathVariable @Positive Long eventId,
                                                                          @RequestBody @Valid EventRequestsPrivateUpdateRequestDto dto) {
        return eventRequestService.updatePrivateEventRequests(eventId, userId, dto);
    }
}
