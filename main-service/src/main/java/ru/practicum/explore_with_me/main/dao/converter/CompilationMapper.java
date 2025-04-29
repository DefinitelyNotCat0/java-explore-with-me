package ru.practicum.explore_with_me.main.dao.converter;

import org.mapstruct.*;
import ru.practicum.explore_with_me.main.dao.entity.CompilationEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationDto;

import java.util.Map;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "name", source = "title")
    CompilationEntity toCompilationEntity(CompilationAdminCreateRequestDto compilationAdminCreateRequestDto,
                                          @Context Set<EventEntity> eventEntitySet);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "name", source = "title")
    CompilationEntity toCompilationEntity(CompilationAdminUpdateRequestDto compilationAdminUpdateRequestDto,
                                          @Context Set<EventEntity> eventEntitySet);

    @Mapping(target = "title", source = "name")
    CompilationDto toCompilationDto(CompilationEntity compilationEntity,
                                    @Context Map<Long, Integer> compilationViewsMap);

    @AfterMapping
    default void toCompilationEntity(@MappingTarget CompilationEntity target,
                                     CompilationAdminCreateRequestDto compilationAdminCreateRequestDto,
                                     @Context Set<EventEntity> eventEntitySet) {
        target.setEvents(eventEntitySet);
    }

    @AfterMapping
    default void toCompilationEntity(@MappingTarget CompilationEntity target,
                                     CompilationAdminUpdateRequestDto compilationAdminUpdateRequestDto,
                                     @Context Set<EventEntity> eventEntitySet) {
        target.setEvents(eventEntitySet);
    }
}
