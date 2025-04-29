package ru.practicum.shareit.user.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Data
@Repository
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> usersMap = new HashMap<>();

    @Override
    public Map<Long, User> getUsersMap() {
        return usersMap;
    }

    @Override
    public UserDto createUser(User user) {
        User newUser = new User();
        newUser.setId(getNextId());
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        usersMap.put(newUser.getId(), newUser);
        log.info("Пользователь был создан: %s".formatted(newUser));
        return UserMapper.buildUserDto(newUser);
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.buildUserDto(usersMap.get(id));
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        User updateUser = usersMap.get(userId);
        log.info("Обновление пользователя: %s".formatted(updateUser));
        Optional.ofNullable(user.getName()).ifPresent(updateUser::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(updateUser::setEmail);
        log.info("Пользователь обновлен: %s".formatted(updateUser));
        return UserMapper.buildUserDto(updateUser);
    }

    @Override
    public void deleteUser(Long id) {
        usersMap.remove(id);
        log.info("Пользователь с id: %s удален".formatted(id));
    }

    public long getNextId() {
        long currentMaxId = usersMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
