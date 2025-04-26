package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Long itemId);
    ItemDto createItem(Item item);
    ItemDto updateItem(Long id, Item item);
    List<ItemDto> searchItem(String search);
    List<ItemDto> getAllFromUser(Long userId);
    void deleteItem(Long itemId);
}
