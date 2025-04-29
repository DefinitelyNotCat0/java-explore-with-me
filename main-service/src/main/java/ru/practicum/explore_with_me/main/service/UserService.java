package ru.practicum.explore_with_me.main.service;

import ru.practicum.explore_with_me.main.dto.user.UserAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.user.UserAdminDto;

import java.util.List;

public interface UserService {

    List<UserAdminDto> getUserList(int from, int size, List<Long> ids);

    UserAdminDto createUser(UserAdminCreateRequestDto dto);

    void deleteUser(Long id);
}
