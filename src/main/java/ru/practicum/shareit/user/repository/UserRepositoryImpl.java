package ru.practicum.shareit.user.repository;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Repository
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> usersMap = new HashMap<>();

    @Override
    public UserDto createUser(User user) {
        User newUser = new User();
        newUser.setId(getNextId());
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        usersMap.put(newUser.getId(), newUser);
        return UserMapper.buildUserDto(newUser);
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.buildUserDto(usersMap.get(id));
    }

    @Override
    public UserDto updateUser(User user) {
        User updateUser = usersMap.get(user.getId());
        Optional.ofNullable(user.getName()).ifPresent(updateUser::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(updateUser::setEmail);
        return UserMapper.buildUserDto(updateUser);
    }

    @Override
    public void deleteUser(Long id) {
        usersMap.remove(id);
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
