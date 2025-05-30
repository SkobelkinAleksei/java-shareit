package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto getItem(Long itemId) {
        Item item = getItemOrThrow(itemId);
        return ItemMapper.buildItemDto(item, getAllCommentsByItemId(itemId));
    }

    @Transactional
    @Override
    public ItemDto createItem(Item item, Long userId) {
        getUserOrThrow(userId);
        item.setOwnerId(userId);
        return ItemMapper.buildItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Item item, Long itemId) {
        Item newItem = getItemOrThrow(itemId);
        getUserOrThrow(userId);

        if (!newItem.getOwnerId().equals(userId)) {
            throw new NotFoundException(String.format("Пользователь %s не является владельцем вещи %s", userId, itemId));
        }

        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }
        if (item.getRequestId() != null) {
            newItem.setRequestId(item.getRequestId());
        }

        return ItemMapper.buildItemDto(itemRepository.save(newItem));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        }

        String textTrim = text.trim();
        return itemRepository.search(textTrim).stream()
                .map(ItemMapper::buildItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllFromUser(Long userId) {
        getUserOrThrow(userId);

        List<Item> itemsByOwnerId = itemRepository.findItemsByOwnerId(userId);

        return itemsByOwnerId.stream()
                .map(ItemMapper::buildItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteItem(Long itemId) {
        getItemOrThrow(itemId);
        itemRepository.deleteById(itemId);
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        User user = getUserOrThrow(userId);
        Item item = getItemOrThrow(itemId);
        Comment comment1 = CommentMapper.toComment(comment,
                user,
                item
        );

        List<Booking> bookings = bookingRepository.getAllUserBookings(userId, itemId, LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new ValidationException("Нужно создать бронирование, только потом комментарий");
        }
        return CommentMapper.buildToCommentDto(commentRepository.save(comment1));
    }

    @Override
    public List<CommentDto> getAllCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return comments
                .stream()
                .map(CommentMapper::buildToCommentDto)
                .collect(Collectors.toList());
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item с id " + itemId + " не найден"));
    }
}
