package ru.practicum.shareit.validator;

import jakarta.validation.ValidationException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

@Slf4j
@RequiredArgsConstructor
@Data
@Component
public class ValidatorUser {
    private final UserRepositoryImpl userRepository;

    public void validMail(User user) {
        log.info("Валидация на email: %s".formatted(user.getName()));
        for(User validUser : userRepository.getUsersMap().values()) {
            if (validUser.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже есть");
            }
        }
        log.info("Валидация email пройдена");
    }

    public void validUserId(Long id) {
        if(!userRepository.getUsersMap().containsKey(id)) {
            throw new NotFoundException("Пользователь с таким id не был найден");
        }
        log.info("Пользователь с таким id найден: %s".formatted(id));
    }

}
