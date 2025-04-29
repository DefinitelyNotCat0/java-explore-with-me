package ru.practicum.explore_with_me.main.dao.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.explore_with_me.main.dao.model.Location;
import ru.practicum.explore_with_me.main.dto.misc.LocationDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);
}
