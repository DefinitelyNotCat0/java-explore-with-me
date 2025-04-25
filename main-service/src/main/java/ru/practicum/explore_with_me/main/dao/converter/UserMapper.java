package ru.practicum.explore_with_me.main.dao.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dto.user.UserAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.user.UserAdminDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserEntity toUserEntity(UserAdminCreateRequestDto userAdminCreateRequestDto);

    UserAdminDto toUserAdminDto(UserEntity userEntity);
}
