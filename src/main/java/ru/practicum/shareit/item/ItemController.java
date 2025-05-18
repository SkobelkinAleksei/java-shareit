package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        log.info("Начинаем получение item по id: %s".formatted(itemId));
        return itemService.getItem(itemId);
    }

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid Item item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Начинаем создание item: %s".formatted(item));
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody Item item,
                              @PathVariable Long itemId) {
        log.info("Начинаем обновление item: %s".formatted(item));
        return itemService.updateItem(userId, item, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("Начинаем поиск item: %s".formatted(text));
        return itemService.searchItem(text);
    }

    @GetMapping
    public List<ItemDto> getAllFromUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Начинаем получение всех item y user c id: %s".formatted(userId));
        return itemService.getAllFromUser(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        log.info("Начинаем удаление item c id: %s".formatted(itemId));
        itemService.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody Comment dto) {
        return itemService.addComment(userId, itemId, dto);
    }
}
