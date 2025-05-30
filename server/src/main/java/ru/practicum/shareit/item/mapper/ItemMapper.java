package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemMapper {
    public static ItemDto buildItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable() != null ? item.getAvailable() : true);
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);
        itemDto.setComments(new ArrayList<>());
        return itemDto;
    }

    public static ItemDto buildItemDto(Item item, List<CommentDto> commentDto) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable() != null ? item.getAvailable() : true);
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);
        itemDto.setComments(commentDto);
        return itemDto;
    }
}
