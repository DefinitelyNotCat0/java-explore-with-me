package ru.practicum.explore_with_me.main.dto.eventrequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;

import java.time.LocalDateTime;

@Data
public class EventRequestDto {

    private Long id;

    private Long event;

    private Long requester;

    private EventRequestStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime created;
}
