package ru.practicum.shareit.item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ItemControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testGetItem() throws Exception {
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        when(itemService.getItem(itemId)).thenReturn(itemDto);

        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));

        verify(itemService).getItem(itemId);
    }

    @Test
    public void testCreateItem() throws Exception {
        Long userId = 1L;
        Item item = new Item();
        item.setName("Test");
        item.setDescription("Test description");
        item.setAvailable(true);

        String jsonBody = objectMapper.writeValueAsString(item);

        ItemDto returnedDto = new ItemDto();
        returnedDto.setId(1L);
        when(itemService.createItem(any(Item.class), eq(userId))).thenReturn(returnedDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService).createItem(any(Item.class), eq(userId));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Long userId = 1L;
        Long itemId = 2L;
        Item updateItem = new Item();
        updateItem.setName("Updated");
        String jsonBody = objectMapper.writeValueAsString(updateItem);

        ItemDto returnedDto = new ItemDto();
        returnedDto.setId(itemId);
        when(itemService.updateItem(eq(userId), any(Item.class), eq(itemId))).thenReturn(returnedDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));

        verify(itemService).updateItem(eq(userId), any(Item.class), eq(itemId));
    }

    @Test
    public void testSearchItem() throws Exception {
        String searchText = "test";
        List<ItemDto> results = List.of(new ItemDto());
        when(itemService.searchItem(searchText)).thenReturn(results);

        mockMvc.perform(get("/items/search")
                        .param("text", searchText))
                .andExpect(status().isOk());

        verify(itemService).searchItem(searchText);
    }

    @Test
    public void testGetAllFromUser() throws Exception {
        Long userId = 1L;
        List<ItemDto> results = List.of(new ItemDto());
        when(itemService.getAllFromUser(userId)).thenReturn(results);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService).getAllFromUser(userId);
    }

    @Test
    public void testDeleteItem() throws Exception {
        Long itemId = 1L;

        doNothing().when(itemService).deleteItem(itemId);

        mockMvc.perform(delete("/items/{itemId}", itemId))
                .andExpect(status().isOk());

        verify(itemService).deleteItem(itemId);
    }

    @Test
    public void testAddComment() throws Exception {
        Long userId = 1L;
        Long itemId = 2L;

        Item mockItem = new Item();
        mockItem.setId(itemId);

        User mockUser = new User();
        mockUser.setId(userId);

        Comment comment = new Comment();
        comment.setText("Nice!");
        comment.setItem(mockItem);
        comment.setAuthor(mockUser);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Nice!");

        when(itemService.addComment(eq(userId), eq(itemId), any(Comment.class))).thenReturn(commentDto);

        String jsonBody = objectMapper.writeValueAsString(comment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Nice!"));

        verify(itemService).addComment(eq(userId), eq(itemId), any(Comment.class));
    }
}