package ru.practicum.explore_with_me.main.dao.converter;

import org.mapstruct.*;
import ru.practicum.explore_with_me.main.dao.entity.CategoryEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dto.event.EventCompilationDto;
import ru.practicum.explore_with_me.main.dto.event.EventDto;
import ru.practicum.explore_with_me.main.dto.event.EventPrivateCreateRequestDto;

import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {LocationMapper.class})
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    EventEntity toEventEntity(EventPrivateCreateRequestDto eventPrivateCreateRequestDto,
                              @Context UserEntity userEntity,
                              @Context CategoryEntity categoryEntity);

    @Mapping(target = "createdOn", source = "created")
    @Mapping(target = "publishedOn", source = "published")
    @Mapping(target = "views", constant = "0")
    EventDto toEventDto(EventEntity eventEntity);

    @Mapping(target = "createdOn", source = "created")
    @Mapping(target = "publishedOn", source = "published")
    EventDto toEventDto(EventEntity eventEntity,
                        @Context String urlPath,
                        @Context Map<String, Integer> hitCountStatistic);

    EventCompilationDto toEventCompilationDto(EventEntity eventEntity);

    @AfterMapping
    default void toEventEntity(@MappingTarget EventEntity target,
                               EventPrivateCreateRequestDto eventPrivateCreateRequestDto,
                               @Context UserEntity userEntity,
                               @Context CategoryEntity categoryEntity) {
        target.setInitiator(userEntity);
        target.setCategory(categoryEntity);
    }

    @AfterMapping
    default void toEventDto(@MappingTarget EventDto target,
                            EventEntity eventEntity,
                            @Context String urlPath,
                            @Context Map<String, Integer> hitCountStatistic) {
        target.setViews(hitCountStatistic
                .getOrDefault(urlPath + eventEntity.getId(), 0));
    }
}
