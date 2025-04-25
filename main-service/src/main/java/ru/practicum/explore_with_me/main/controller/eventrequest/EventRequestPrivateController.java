package ru.practicum.explore_with_me.main.controller.eventrequest;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestDto;
import ru.practicum.explore_with_me.main.service.EventRequestService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class EventRequestPrivateController {

    private final EventRequestService eventRequestService;

    @GetMapping
    public List<EventRequestDto> getPrivateEventRequestList(@PathVariable @Positive Long userId) {
        return eventRequestService.getPrivateEventRequestList(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto createPrivateEventRequest(@PathVariable @Positive Long userId,
                                                     @RequestParam @Positive Long eventId) {
        return eventRequestService.createPrivateEventRequest(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public EventRequestDto cancelPrivateEventRequest(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long requestId) {
        return eventRequestService.cancelPrivateEventRequest(requestId, userId);
    }
}
