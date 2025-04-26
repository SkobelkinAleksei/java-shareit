package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItem(Long itemId) {
        return itemRepository.getItem(itemId);
    }

    @Override
    public ItemDto createItem(Item item) {
        return itemRepository.createItem(item);
    }

    @Override
    public ItemDto updateItem(Long id, Item item) {
        item.setId(id);
        return itemRepository.updateItem(item);
    }

    @Override
    public List<ItemDto> searchItem(String search) {
        return itemRepository.searchItem(search);
    }

    @Override
    public List<ItemDto> getAllFromUser(Long userId) {
        return itemRepository.getAllFromUser(userId);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteItem(itemId);
    }
}
