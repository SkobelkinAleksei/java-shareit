package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createItemRequest(
            @RequestBody CreateItemRequestDto requestDto,
            @RequestHeader(value = userIdHeader, required = true) @Positive Long userId) {

        log.info(("%s'---------------------------------'").formatted(requestDto.getDescription()));
        return requestService.createItemRequest(userId, requestDto.getDescription());
    }

    @GetMapping
    public List<ItemRequestDto> getByUserId(
            @RequestHeader(value = userIdHeader, required = true) @Positive Long userId) {
        return requestService.getByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequest() {
        return requestService.getAllItemRequest();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@PathVariable Long requestId) {
        return requestService.getItemRequest(requestId);
    }
}
