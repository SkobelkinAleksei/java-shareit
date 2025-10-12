package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

public class ItemRequestServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItemRequest_ShouldCreateAndReturnDto() {
        Long userId = 1L;
        String description = "Need a ladder";

        when(userService.getUser(userId)).thenReturn(new UserDto());

        ArgumentCaptor<ItemRequest> captor = ArgumentCaptor.forClass(ItemRequest.class);

        when(itemRequestRepository.save(any())).thenAnswer(invocation -> {
            ItemRequest ir = invocation.getArgument(0);
            ir.setId(100L);
            return ir;
        });

        ItemRequestDto result = service.createItemRequest(userId, description);

        verify(userService).getUser(userId);
        verify(itemRequestRepository).save(captor.capture());

        assertNotNull(result);
        assertEquals(description, result.getDescription());

        ItemRequest savedEntity = captor.getValue();

        assertEquals(userId, savedEntity.getRequestorId());

        assertEquals(savedEntity.getDescription(), result.getDescription());

        assertTrue(savedEntity.getCreated().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(savedEntity.getCreated().isAfter(LocalDateTime.now().minusSeconds(5)));

        assertEquals(savedEntity.getCreated(), result.getCreated());
        assertEquals(savedEntity.getId(), result.getId());
    }

    @Test
    void getItemRequest_ShouldReturnFullDetails() {
        Long requestId = 42L;

        ItemRequest itemReq = new ItemRequest();
        itemReq.setId(requestId);
        itemReq.setDescription("desc");
        itemReq.setCreated(LocalDateTime.of(2023, 10, 10, 12, 0));
        itemReq.setRequestorId(123L);

        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemReq));

        List<Item> items = List.of();

        when(itemRepository.findAllByRequestId(requestId)).thenReturn(items);

        ItemRequestDto result = service.getItemRequest(requestId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals("desc", result.getDescription());

        assertEquals(items.size(), result.getItems() != null ? result.getItems().size() : 0);

        verify(itemRepository).findAllByRequestId(requestId);
    }
}