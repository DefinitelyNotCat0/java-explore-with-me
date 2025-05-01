package ru.practicum.explore_with_me.main.dao.converter;

import org.mapstruct.*;
import ru.practicum.explore_with_me.main.dao.entity.EventCommentEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentDto;
import ru.practicum.explore_with_me.main.dto.eventcomment.EventCommentPrivateCreateRequestDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventCommentMapper {

    @Mapping(target = "authorName",
            expression = "java(eventCommentEntity.getAuthor() != null ? " +
                    "eventCommentEntity.getAuthor().getName() : null)")
    EventCommentDto toEventCommentDto(EventCommentEntity eventCommentEntity);

    EventCommentEntity toEventCommentEntity(EventCommentPrivateCreateRequestDto dto,
                                            @Context UserEntity userEntity,
                                            @Context EventEntity eventEntity);

    @AfterMapping
    default void toEventCommentEntity(@MappingTarget EventCommentEntity target,
                                      EventCommentPrivateCreateRequestDto dto,
                                      @Context UserEntity userEntity,
                                      @Context EventEntity eventEntity) {
        target.setAuthor(userEntity);
        target.setEvent(eventEntity);
    }
}
