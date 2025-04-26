package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
        return itemService.getItem(itemId);
    }

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid Item item) {
        return itemService.createItem(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long id, @RequestBody Item item) {
        return itemService.updateItem(id, item);
    }

    @GetMapping("/{search}")
    public List<ItemDto> searchItem(@RequestParam("text") String search) {
        return itemService.searchItem(search);
    }

    @GetMapping
    public List<ItemDto> getAllFromUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllFromUser(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }
}
