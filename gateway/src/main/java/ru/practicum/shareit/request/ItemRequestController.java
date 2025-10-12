package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(userIdHeader) Long userId,
                                                @Valid @RequestBody CreateItemRequestDto createItemRequestDto) {
        return itemRequestClient.createItemRequest(userId, createItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader(userIdHeader) Long userId) {
        return itemRequestClient.getByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader(userIdHeader) Long userId,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(name = "size", defaultValue = "50") @Positive Integer size) {
        return itemRequestClient.getAllItemRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(userIdHeader) Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        return itemRequestClient.getItemRequest(userId, requestId);
    }
}
