package ru.practicum.explore_with_me.main.dto.eventcomment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventCommentPrivateUpdateRequestDto {

    @NotBlank
    @Size(max = 7000)
    private String message;
}
