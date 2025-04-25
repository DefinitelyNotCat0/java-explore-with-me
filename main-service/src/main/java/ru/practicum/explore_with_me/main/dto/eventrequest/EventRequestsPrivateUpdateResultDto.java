package ru.practicum.explore_with_me.main.dto.eventrequest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestsPrivateUpdateResultDto {

    private List<EventRequestDto> confirmedRequests;

    private List<EventRequestDto> rejectedRequests;
}
