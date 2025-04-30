package ru.practicum.explore_with_me.main.controller.eventcomment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentDto;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentPrivateCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentPrivateUpdateRequestDto;
import ru.practicum.explore_with_me.main.service.EventCommentService;

@Slf4j
@RestController
@Validated
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
public class EventCommentPrivateController {

    private final EventCommentService eventCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventCommentDto createPrivateEventComment(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId,
                                                     @RequestBody @Valid EventCommentPrivateCreateRequestDto dto) {
        log.debug("Controller: createPrivateEventComment");
        return eventCommentService.createPrivateEventComment(eventId, userId, dto);
    }

    @PatchMapping("/{commentId}")
    public EventCommentDto updatePrivateEventComment(@PathVariable @Positive Long commentId,
                                                     @PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId,
                                                     @RequestBody @Valid EventCommentPrivateUpdateRequestDto dto) {
        log.debug("Controller: updatePrivateEventComment");
        return eventCommentService.updatePrivateEventComment(commentId, userId, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrivateEventComment(@PathVariable @Positive Long id,
                                          @PathVariable @Positive Long userId) {
        log.debug("Controller: deletePrivateEventComment");
        eventCommentService.deletePrivateEventComment(id, userId);
    }
}
