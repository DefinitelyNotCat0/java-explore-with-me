package ru.practicum.explore_with_me.stat.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explore_with_me.stat.server.service.StatService;
import ru.practicum.explore_with_me.stats.dto.AppUriHitCountDto;
import ru.practicum.explore_with_me.stats.dto.HitAddRequestDto;
import ru.practicum.explore_with_me.stats.dto.HitDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatController.class)
class StatControllerTest {

    private final LocalDateTime localDateTime1 = LocalDateTime.of(
            2025,
            1,
            12,
            10,
            11,
            0
    );
    private final HitAddRequestDto hitAddRequestDto1 = new HitAddRequestDto(
            "app_test1",
            "uri_test1",
            "1.1.1.1",
            localDateTime1
    );
    private final AppUriHitCountDto appUriHitCountDto1 = new AppUriHitCountDto(
            "app_1",
            "uri_1",
            10
    );
    private final AppUriHitCountDto appUriHitCountDto2 = new AppUriHitCountDto(
            "app_2",
            "uri_2",
            6
    );
    MockedStatic<LocalDateTime> localDateTimeMocked;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    StatService statService;

    @Autowired
    private MockMvc mvc;

    @Test
    void addHit() throws Exception {
        when(statService.addHit(any()))
                .thenReturn(new HitDto());

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(hitAddRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void getAllUsers() throws Exception {
        when(statService.getAppUriHitCountStatistic(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(appUriHitCountDto1, appUriHitCountDto2));

        mvc.perform(get("/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].app",
                        Matchers.containsInAnyOrder(
                                appUriHitCountDto1.getApp(),
                                appUriHitCountDto2.getApp())
                ))
                .andExpect(jsonPath("$[*].uri",
                        Matchers.containsInAnyOrder(
                                appUriHitCountDto1.getUri(),
                                appUriHitCountDto2.getUri())
                ));
    }
}