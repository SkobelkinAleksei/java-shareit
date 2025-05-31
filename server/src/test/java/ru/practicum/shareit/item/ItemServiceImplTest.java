package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.CommentRepository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Test User");

        item = new Item();
        item.setId(1L);
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setOwnerId(user.getId());
    }

    @Test
    public void testGetItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemId(1L)).thenReturn(Collections.emptyList());

        ItemDto result = itemService.getItem(1L);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        verify(itemRepository).findById(1L);
        verify(commentRepository).findAllByItemId(1L);
    }

    @Test
    public void testCreateItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class)))
                .thenAnswer(invocation -> {
                    Item savedItem = invocation.getArgument(0);
                    savedItem.setId(100L);
                    return savedItem;
        });

        Item newItem = new Item();
        newItem.setName("New Item");
        newItem.setDescription("New Description");
        newItem.setAvailable(true);

        ItemDto result = itemService.createItem(newItem, 1L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(userRepository).findById(1L);
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    public void testUpdateItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Item updateData = new Item();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");
        updateData.setAvailable(false);

        ItemDto result = itemService.updateItem(1L, updateData, 1L);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertFalse(result.getAvailable());
    }

    @Test
    public void testUpdateItemNotOwnerShouldThrow() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));

        Item updateData = new Item();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(2L, updateData, 1L));

        assertTrue(exception.getMessage().contains("не является владельцем"));
    }

    @Test
    public void testSearchItemReturnsResults() {
        String searchText = "item";

        when(itemRepository.search(searchText)).thenReturn(List.of(item));

        List<ItemDto> results = itemService.searchItem(searchText);

        assertFalse(results.isEmpty());

        verify(itemRepository).search(searchText);
    }

    @Test
    public void testSearchItemEmptyReturnsEmpty() {
        List<ItemDto> results = itemService.searchItem(" ");
        assertTrue(results.isEmpty());
        verifyNoInteractions(itemRepository);
    }

    @Test
    public void testGetAllFromUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findItemsByOwnerId(1L)).thenReturn(List.of(item));

        List<ItemDto> results = itemService.getAllFromUser(1L);

        assertFalse(results.isEmpty());
        verify(itemRepository).findItemsByOwnerId(1L);
    }

    @Test
    public void testDeleteItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        doNothing().when(itemRepository).deleteById(1L);

        itemService.deleteItem(1L);

        verify(itemRepository).deleteById(1L);
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment();
        comment.setText("Nice!");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment c = invocation.getArgument(0);
            c.setId(10L);
            return c;
        });

        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now().minusDays(2));
        booking.setEndTime(LocalDateTime.now().plusDays(2));

        when(bookingRepository.getAllUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        CommentDto result = itemService.addComment(1L, 1L, comment);

        assertNotNull(result);
        assertEquals("Nice!", result.getText());
        assertEquals(user.getName(), result.getAuthorName());
    }

    @Test
    public void testAddCommentNoBookingShouldThrowValidationException() {
        Comment comment = new Comment();
        comment.setText("Nice!");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(bookingRepository.getAllUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class,
                () -> itemService.addComment(1L, 1L, comment));
    }
}