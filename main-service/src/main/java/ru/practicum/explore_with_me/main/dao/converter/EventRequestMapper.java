package ru.practicum.explore_with_me.main.dao.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dao.entity.EventRequestEntity;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestDto;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestStatus;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {EventMapper.class, UserMapper.class})
public interface EventRequestMapper {

    default void enrichEventRequestEntity(EventRequestEntity target,
                                          EventEntity eventEntity,
                                          UserEntity requesterEntity) {
        target.setEvent(eventEntity);
        target.setRequester(requesterEntity);
        target.setStatus(eventEntity.isRequestModeration() && eventEntity.getParticipantLimit() > 0 ?
                EventRequestStatus.PENDING : EventRequestStatus.CONFIRMED);
    }

    @Mapping(target = "event",
            expression = "java(eventRequestEntity.getEvent() != null ? " +
                    "eventRequestEntity.getEvent().getId() : null)")
    @Mapping(target = "requester",
            expression = "java(eventRequestEntity.getRequester() != null ? " +
                    "eventRequestEntity.getRequester().getId() : null)")
    EventRequestDto toEventRequestDto(EventRequestEntity eventRequestEntity);
}
