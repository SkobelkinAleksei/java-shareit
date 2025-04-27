package ru.practicum.shareit.validator;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;

@Slf4j
@RequiredArgsConstructor
@Data
@Component
public class ValidatorItem {
    private final ItemRepositoryImpl itemRepository;

    public void validItemId(Long itemId) {
        log.info("Проверяем, что есть item с id: %s".formatted(itemId));
        if (!itemRepository.getItems().containsKey(itemId)) {
            throw new NullPointerException("Item c таким id не найден");
        }
        log.info("Найден Item с id: %s".formatted(itemId));
    }
}
