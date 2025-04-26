package ru.practicum.shareit.item.repository;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Data
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public ItemDto getItem(Long itemId) {
        return ItemMapper.buildItemDto(items.get(itemId));
    }

    @Override
    public ItemDto createItem(Item item) {
        Item newItem = new Item();
        newItem.setId(getNextId());
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setAvailable(item.getAvailable());
        newItem.setOwner(item.getOwner());
        newItem.setRequest(item.getRequest());
        items.put(newItem.getId(), newItem);
        return ItemMapper.buildItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Item item) {
        Item updateItem = items.get(item.getId());
        Optional.ofNullable(item.getName()).ifPresent(updateItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(updateItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(updateItem::setAvailable);
        return ItemMapper.buildItemDto(updateItem);
    }

    @Override
    public List<ItemDto> searchItem(String search) {
        List<ItemDto> itemList = new ArrayList<>();
        String searchTrim = search.trim().toLowerCase();

        for (Item item : items.values()) {
            if (item.getName().trim().toLowerCase().equals(searchTrim)
                    && item.getAvailable())
            {
                itemList.add(ItemMapper.buildItemDto(item));
            };
        }
        return itemList;
    }

    @Override
    public List<ItemDto> getAllFromUser(Long userId) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(Item item : items.values()) {
            if (item.getOwner().equals(userId)) {
                itemDtoList.add(ItemMapper.buildItemDto(item));
            }
        }
        return itemDtoList;
    }

    @Override
    public void deleteItem(Long itemId) {
        items.remove(itemId);
    }

    public long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
