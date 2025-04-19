package ru.practicum.explore_with_me.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUriHitCountDto {

    private String app;

    private String uri;

    private Integer hits;
}
