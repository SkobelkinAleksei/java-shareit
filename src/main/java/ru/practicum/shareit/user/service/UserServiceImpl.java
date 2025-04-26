package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    //Добавить валидацию?

    @Override
    public UserDto createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public UserDto getUser(Long id) {
        return userRepository.getUser(id);
    }

    @Override
    public UserDto updateUser(Long id, User user) {
        user.setId(id);
        return userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }
}
