package ru.practicum.explore_with_me.main.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;
import ru.practicum.explore_with_me.main.dto.misc.EventSort;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventSearchParamDto {

    private String text;

    private Set<Long> categories;

    private Boolean paid;

    @DateTimeFormat(pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime rangeStart;

    @DateTimeFormat(pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime rangeEnd;

    private Boolean onlyAvailable;

    private EventSort sort = EventSort.EVENT_DATE;

    @PositiveOrZero
    private int from = 0;

    @Positive
    private int size = 10;
}
