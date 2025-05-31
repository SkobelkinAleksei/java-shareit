package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto getItemRequest(Long id);

    List<ItemRequestDto> getAllItemRequest();

    ItemRequestDto createItemRequest(Long userId, String text);

    List<ItemRequestDto> getByUserId(Long userId);
}
