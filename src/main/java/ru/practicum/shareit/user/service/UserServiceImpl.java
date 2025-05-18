package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.ValidatorUser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidatorUser validator;

    @Override
    public UserDto createUser(User user) {
        List<User> users = userRepository.findAll();
        Set<String> existingEmail = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        validator.validMail(user, existingEmail);
        userRepository.save(user);

        return UserMapper.buildToUserDto(user);
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.buildToUserDto(getUserOrThrow(id));
    }

    @Override
    public UserDto updateUser(Long id, User user) {
        User newUser = getUserOrThrow(id);

        List<User> users = userRepository.findAll();
        Set<String> existingEmails = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        validator.validMail(user, existingEmails);

        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }

        return UserMapper.buildToUserDto(userRepository.save(newUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}
