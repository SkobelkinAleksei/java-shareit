package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    ItemDto getItem(Long itemId);
    ItemDto createItem(Item item, Long userId);
    ItemDto updateItem(Long userId, Item item, Long itemId);
    List<ItemDto> searchItem(String search);
    List<ItemDto> getAllFromUser(Long userId);
    void deleteItem(Long itemId);
}
