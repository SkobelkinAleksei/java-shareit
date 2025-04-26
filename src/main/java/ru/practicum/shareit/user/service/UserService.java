package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto createUser(User user);
    UserDto getUser(Long id);
    UserDto updateUser(Long id, User user);
    void deleteUser(Long id);
}
