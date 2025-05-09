package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

public interface UserRepository {
    UserDto createUser(User user);

    UserDto getUser(Long id);

    UserDto updateUser(Long userId, User user);

    Map<Long, UserDto> getUsersMap();

    void deleteUser(Long id);
}
