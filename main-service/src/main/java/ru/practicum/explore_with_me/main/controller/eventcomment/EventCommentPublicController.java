package ru.practicum.explore_with_me.main.controller.eventcomment;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentDto;
import ru.practicum.explore_with_me.main.service.EventCommentService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
public class EventCommentPublicController {

    private final EventCommentService eventCommentService;

    @GetMapping
    public List<EventCommentDto> getEventCommentList(@PathVariable @Positive Long eventId) {
        log.debug("Controller: getEventCommentList");
        return eventCommentService.getEventCommentList(eventId);
    }
}
