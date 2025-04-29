package ru.practicum.explore_with_me.main.dto.compilation;

import lombok.Data;
import ru.practicum.explore_with_me.main.dto.event.EventCompilationDto;

import java.util.Set;

@Data
public class CompilationDto {

    private Long id;

    private boolean pinned;

    private String title;

    private Set<EventCompilationDto> events;
}
