package ru.practicum.explore_with_me.main.dto.eventcomment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.explore_with_me.main.constant.MainServiceConstants;

import java.time.LocalDateTime;

@Data
public class EventCommentDto {

    private Long id;

    private String message;

    private String authorName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MainServiceConstants.DATE_TIME_FORMAT)
    private LocalDateTime lastUpdateDay;
}
