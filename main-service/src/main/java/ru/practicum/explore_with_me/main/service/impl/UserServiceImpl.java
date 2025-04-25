package ru.practicum.explore_with_me.main.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.practicum.explore_with_me.main.dao.converter.UserMapper;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;
import ru.practicum.explore_with_me.main.dao.repository.UserRepository;
import ru.practicum.explore_with_me.main.dto.user.UserAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.user.UserAdminDto;
import ru.practicum.explore_with_me.main.exception.ConflictException;
import ru.practicum.explore_with_me.main.exception.NotFoundException;
import ru.practicum.explore_with_me.main.service.UserService;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserAdminDto> getUserList(int from, int size, List<Long> ids) {
        log.debug("Get user list with from = {} and size = {} and ids size = {}",
                from, size, ids == null ? 0 : ids.size());
        Pageable pageable = PageRequest.of(from / size, size);
        Page<UserEntity> userEntityPage = ObjectUtils.isEmpty(ids) ?
                userRepository.findAll(pageable) : userRepository.findAllByIdIn(ids, pageable);
        return userEntityPage
                .stream()
                .map(userMapper::toUserAdminDto)
                .toList();
    }

    @Override
    @Transactional
    public UserAdminDto createUser(UserAdminCreateRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User already exists");
        }

        UserEntity userEntity = userMapper.toUserEntity(dto);
        userRepository.save(userEntity);
        log.debug("User was created with id = {}", userEntity.getId());
        return userMapper.toUserAdminDto(userEntity);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User does not exist with id = " + id);
        }
        userRepository.deleteById(id);
        log.debug("User with id = {} was deleted", id);
    }
}
