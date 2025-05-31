package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService{
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;


    @Override
    public ItemRequestDto getItemRequest(Long id) {
        ItemRequestDto itemRequest = itemRequestRepository.findById(id)
                .map(ItemRequestMapper::buildItemRequestDto)
                .orElseThrow(() -> new NotFoundException("ItemRequest с id " + id + " не найден"));

        List<ItemDto> items = itemRepository.findAllByRequestId(id).stream()
                .map(ItemMapper::buildItemDto)
                .toList();

        itemRequest.setItems(items);
        return itemRequest;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequest() {
        return itemRequestRepository.findAll().stream()
                .map(ItemRequestMapper::buildItemRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long userId, String text) {
        if (userService.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestorId(userId);
        itemRequest.setDescription(text);
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.buildItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getByUserId(Long userId) {
        return itemRequestRepository.findAll().stream()
                .map(ItemRequestMapper::buildItemRequestDto)
                .filter(dto -> dto.getRequestorId() != null && dto.getRequestorId().equals(userId))
                .toList();
    }
}
