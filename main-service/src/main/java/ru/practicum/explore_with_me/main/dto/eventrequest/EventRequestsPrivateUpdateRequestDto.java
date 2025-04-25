package ru.practicum.explore_with_me.main.dto.eventrequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EventRequestsPrivateUpdateRequestDto {

    @NotNull
    @NotEmpty
    private List<Long> requestIds;

    @NotNull
    private EventRequestStatus status;
}
