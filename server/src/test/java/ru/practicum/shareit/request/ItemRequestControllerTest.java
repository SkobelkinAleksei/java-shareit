package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ItemRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    public ItemRequestControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createItemRequestShouldReturnCreatedDto() throws Exception {
        // Arrange
        Long userId = 1L;
        String description = "Need a drill";

        CreateItemRequestDto createDto = new CreateItemRequestDto();
        createDto.setDescription(description);

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(10L);
        responseDto.setDescription(description);
        responseDto.setRequestorId(userId);
        responseDto.setCreated(java.time.LocalDateTime.now());

        when(requestService.createItemRequest(eq(userId), eq(description))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"" + description + "\"}")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.requestorId").value(userId));

        verify(requestService, times(1)).createItemRequest(eq(userId), eq(description));
    }

    @Test
    void getByUserIdShouldReturnList() throws Exception {
        Long userId = 2L;

        ItemRequestDto dto1 = new ItemRequestDto();
        dto1.setId(1L);
        dto1.setDescription("desc1");
        dto1.setRequestorId(userId);
        dto1.setCreated(java.time.LocalDateTime.now());

        when(requestService.getByUserId(userId)).thenReturn(List.of(dto1));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto1.getId()))
                .andExpect(jsonPath("$[0].description").value("desc1"));

        verify(requestService, times(1)).getByUserId(userId);
    }

    @Test
    void getAllItemRequestsShouldReturnList() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(2L);
        dto.setDescription("desc");

        when(requestService.getAllItemRequest()).thenReturn(List.of(dto));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.getId()));

        verify(requestService, times(1)).getAllItemRequest();
    }

    @Test
    void getItemRequestShouldReturnDto() throws Exception {
        Long requestId = 5L;

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(requestId);

        when(requestService.getItemRequest(requestId)).thenReturn(dto);

        mockMvc.perform(get("/requests/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId));

        verify(requestService, times(1)).getItemRequest(requestId);
    }
}