package ru.practicum.explore_with_me.stats.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HitDto {

    private Long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}
