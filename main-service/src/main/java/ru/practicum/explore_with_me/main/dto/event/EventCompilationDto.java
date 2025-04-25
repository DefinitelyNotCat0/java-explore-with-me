package ru.practicum.explore_with_me.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;
import ru.practicum.explore_with_me.main.dto.category.CategoryDto;
import ru.practicum.explore_with_me.main.dto.user.InitiatorDto;

import java.time.LocalDateTime;

@Data
public class EventCompilationDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private InitiatorDto initiator;

    private boolean paid;

    private String title;

    private int views;
}
