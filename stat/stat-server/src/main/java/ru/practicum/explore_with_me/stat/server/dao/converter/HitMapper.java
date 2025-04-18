package ru.practicum.explore_with_me.stat.server.dao.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.explore_with_me.stat.server.dao.entity.HitEntity;
import ru.practicum.explore_with_me.stats.dto.HitAddRequestDto;
import ru.practicum.explore_with_me.stats.dto.HitDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HitMapper {

    @Mapping(target = "timestamp", source = "creationDate")
    HitDto toHitDto(HitEntity hitEntity);

    @Mapping(target = "creationDate", source = "timestamp")
    HitEntity toHitEntity(HitAddRequestDto hitAddRequestDto);
}
