package ru.practicum.explore_with_me.stat.server.dao.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.explore_with_me.stat.server.dao.model.AppUriHitCountEntity;
import ru.practicum.explore_with_me.stats.dto.AppUriHitCountDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppUriHitCountMapper {

    AppUriHitCountDto toAppUriHitCountDto(AppUriHitCountEntity appUriHitCountEntity);
}
