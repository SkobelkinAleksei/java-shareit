package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllFromUser(@RequestHeader(userIdHeader) Long userId) {
        return itemClient.getAllFromUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(userIdHeader) Long userId,
                                                               @RequestParam(value = "text", required = false) String text) {
        return itemClient.searchItem(userId, text);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(userIdHeader) Long userId,
                                              @PathVariable("itemId") Long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(userIdHeader) Long userId,
                                             @Valid @RequestBody ItemDto item) {
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(userIdHeader) Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @Valid @RequestBody UpdateItemDto updateItemDto) {
        return itemClient.updateItem(userId, itemId, updateItemDto);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(userIdHeader) Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @Valid @RequestBody CommentDto dto) {
        return itemClient.addComment(userId, itemId, dto);
    }
}
