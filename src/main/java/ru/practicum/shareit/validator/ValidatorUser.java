package ru.practicum.shareit.validator;

import jakarta.validation.ValidationException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Data
@Component
public class ValidatorUser {

    public void validMail(User user, Set<String> existingEmail) {
        log.info("Валидация на email: %s".formatted(user.getName()));
        if (existingEmail.contains(user.getEmail())) {
            throw new ValidationException("Пользователь с таким email уже есть");
        }
        log.info("Валидация email пройдена");
    }
}
