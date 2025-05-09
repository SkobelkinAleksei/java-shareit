package ru.practicum.shareit.validator;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Data
@Component
public class ValidatorItem {

    public void validItemId(Long itemId, Set<Long> existingItemId) {
        log.info("Проверяем, что есть item с id: %s".formatted(itemId));
        if (!existingItemId.contains(itemId)) {
            throw new NullPointerException("Item c таким id не найден");
        }
        log.info("Найден Item с id: %s".formatted(itemId));
    }
}
