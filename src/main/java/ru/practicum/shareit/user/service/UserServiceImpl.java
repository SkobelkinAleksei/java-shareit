package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.ValidatorUser;

import java.util.HashSet;
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
        Set<String> existingEmail = userRepository.getUsersMap().values().stream()
                                                  .map(User::getEmail)
                                                  .collect(Collectors.toSet());
        validator.validMail(user, existingEmail);
        return userRepository.createUser(user);
    }

    @Override
    public UserDto getUser(Long id) {
        Set<Long> existingId = new HashSet<>(userRepository.getUsersMap().keySet());
        validator.validUserId(id, existingId);
        return userRepository.getUser(id);
    }

    @Override
    public UserDto updateUser(Long id, User user) {
        Set<String> existingEmail = userRepository.getUsersMap().values().stream()
                                                  .map(User::getEmail)
                                                  .collect(Collectors.toSet());

        Set<Long> existingId = new HashSet<>(userRepository.getUsersMap().keySet());

        validator.validUserId(id, existingId);
        validator.validMail(user, existingEmail);
        return userRepository.updateUser(id, user);
    }

    @Override
    public void deleteUser(Long id) {
        Set<Long> existingId = new HashSet<>(userRepository.getUsersMap().keySet());
        validator.validUserId(id, existingId);
        userRepository.deleteUser(id);
    }
}
