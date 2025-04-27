package ru.practicum.explore_with_me.main.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.main.dto.event.EventDto;
import ru.practicum.explore_with_me.main.dto.event.EventSearchParamDto;
import ru.practicum.explore_with_me.main.service.EventService;
import ru.practicum.explore_with_me.main.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;
    private final StatsService statsService;

    @GetMapping
    public List<EventDto> getEventList(@Valid EventSearchParamDto eventSearchParamDto, HttpServletRequest request) {
        log.debug("Controller: getEventList");
        return eventService.getEventList(eventSearchParamDto, request);
    }

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Controller: getEventById");
        return eventService.getEventById(id, request);
    }
}
