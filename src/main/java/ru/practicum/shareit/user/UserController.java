package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid User user) {
        log.info("Начинается создание пользователя: %s".formatted(user));
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody User user) {
        log.info("Начинаем обновление пользователя c id: %s".formatted(userId));
        return userService.updateUser(userId, user);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Начинаем поиск пользователя c id: %s".formatted(userId));
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Начинаем удаление пользователя с id: %s".formatted(userId));
        userService.deleteUser(userId);
    }
}
