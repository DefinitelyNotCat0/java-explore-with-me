package ru.practicum.explore_with_me.main.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.main.dto.user.UserAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.user.UserAdminDto;
import ru.practicum.explore_with_me.main.service.UserService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserAdminDto> getUserList(@RequestParam(value = "ids", required = false) List<Long> ids,
                                          @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.debug("Controller: getUserList");
        return userService.getUserList(from, size, ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserAdminDto createUser(@RequestBody @Valid UserAdminCreateRequestDto dto) {
        log.debug("Controller: createUser");
        return userService.createUser(dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.debug("Controller: deleteUser");
        userService.deleteUser(userId);
    }
}
