package ru.practicum.explore_with_me.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;
import ru.practicum.explore_with_me.main.dto.category.CategoryDto;
import ru.practicum.explore_with_me.main.dto.misc.LocationDto;
import ru.practicum.explore_with_me.main.dto.user.InitiatorDto;

import java.time.LocalDateTime;

@Data
public class EventDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private InitiatorDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private EventState state;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    private Integer views;
}
