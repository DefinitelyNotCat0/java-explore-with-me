package ru.practicum.explore_with_me.main.controller.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.main.dto.event.EventAdminSearchParamDto;
import ru.practicum.explore_with_me.main.dto.event.EventDto;
import ru.practicum.explore_with_me.main.dto.event.EventUpdateRequestDto;
import ru.practicum.explore_with_me.main.service.EventService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getAdminEventList(@Valid EventAdminSearchParamDto searchParamDto) {
        return eventService.getAdminEventList(searchParamDto);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateAdminEvent(@PathVariable @Positive Long eventId,
                                     @RequestBody @Valid EventUpdateRequestDto dto) {
        return eventService.updateAdminEvent(eventId, dto);
    }
}
