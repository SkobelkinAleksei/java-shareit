package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.ValidatorUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidatorUser validator;

    @Override
    public UserDto createUser(User user) {
        validator.validMail(user);
        return userRepository.createUser(user);
    }

    @Override
    public UserDto getUser(Long id) {
        validator.validUserId(id);
        return userRepository.getUser(id);
    }

    @Override
    public UserDto updateUser(Long id, User user) {
        validator.validUserId(id);
        validator.validMail(user);
        return userRepository.updateUser(id, user);
    }

    @Override
    public void deleteUser(Long id) {
        validator.validUserId(id);
        userRepository.deleteUser(id);
    }
}
