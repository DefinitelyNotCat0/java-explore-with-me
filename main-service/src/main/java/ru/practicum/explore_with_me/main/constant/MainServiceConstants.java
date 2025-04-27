package ru.practicum.explore_with_me.main.constant;

import java.time.LocalDateTime;

public class MainServiceConstants {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final LocalDateTime DATE_TIME_MIN = LocalDateTime.of(1970, 1, 1, 0, 0);

    private MainServiceConstants() {
    }
}
