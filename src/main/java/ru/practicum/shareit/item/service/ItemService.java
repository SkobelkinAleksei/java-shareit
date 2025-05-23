package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Long itemId);

    ItemDto createItem(Item item, Long userId);

    ItemDto updateItem(Long userId, Item item, Long itemId);

    List<ItemDto> searchItem(String search);

    List<ItemDto> getAllFromUser(Long userId);

    void deleteItem(Long itemId);

    CommentDto addComment(Long userId, Long itemId, Comment comment);

    List<CommentDto> getAllCommentsByItemId(Long itemId);
}
