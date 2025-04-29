package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.ValidatorItem;
import ru.practicum.shareit.validator.ValidatorUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ValidatorItem validatorItem;
    private final ValidatorUser validatorUser;

    @Override
    public ItemDto getItem(Long itemId) {
        Set<Long> existingItemId = new HashSet<>(itemRepository.getItemsMap().keySet());

        validatorItem.validItemId(itemId, existingItemId);
        return itemRepository.getItem(itemId);
    }

    @Override
    public ItemDto createItem(Item item, Long userId) {
        Set<Long> existingId = new HashSet<>(userRepository.getUsersMap().keySet());

        validatorUser.validUserId(userId, existingId);
        return itemRepository.createItem(item, userId);
    }

    @Override
    public ItemDto updateItem(Long userId, Item item, Long itemId) {
        Set<Long> existingId = new HashSet<>(userRepository.getUsersMap().keySet());
        Set<Long> existingItemId = new HashSet<>(itemRepository.getItemsMap().keySet());

        validatorUser.validUserId(userId, existingId);
        validatorItem.validItemId(itemId, existingItemId);
        return itemRepository.updateItem(userId, item, itemId);
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
