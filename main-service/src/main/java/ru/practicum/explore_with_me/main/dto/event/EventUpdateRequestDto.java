package ru.practicum.explore_with_me.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;
import ru.practicum.explore_with_me.main.dto.misc.LocationDto;

import java.time.LocalDateTime;

@Data
public class EventUpdateRequestDto {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @DateTimeFormat(pattern = MainServiceConstants.DATE_TIME_FORMAT)
    @JsonFormat(pattern = MainServiceConstants.DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private EventState stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
