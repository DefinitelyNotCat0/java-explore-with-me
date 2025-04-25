package ru.practicum.explore_with_me.main.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventAdminSearchParamDto {

    private Set<Long> users;

    private Set<EventState> states;

    private Set<Long> categories;

    @DateTimeFormat(pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime rangeStart;

    @DateTimeFormat(pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime rangeEnd;

    @PositiveOrZero
    private int from = 0;

    @Positive
    private int size = 10;
}
