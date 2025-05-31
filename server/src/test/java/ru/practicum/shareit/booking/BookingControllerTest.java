package ru.practicum.shareit.booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private final String userIdHeader = "X-Sharer-User-Id";

    @Test
    public void createBookingRequestShouldReturnOk() throws Exception {
        String jsonContent = "{ \"item\": { \"id\": 1 }, \"start\": \"2023-10-01T10:00:00\", \"end\": \"2023-10-02T10:00:00\" }";

        when(bookingService.createBookingRequest(anyLong(), any()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);
                    return new BookingsDtoBuilder().build(); // или мокнутый DTO
                });

        mockMvc.perform(post("/bookings")
                        .header(userIdHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());

        verify(bookingService).createBookingRequest(anyLong(), any());
    }

    @Test
    public void approveBookingRequestShouldReturnOk() throws Exception {
        when(bookingService.approveBookingRequest(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new BookingsDtoBuilder().build());

        mockMvc.perform(patch("/bookings/1")
                        .header(userIdHeader, 2)
                        .param("approved", "true"))
                .andExpect(status().isOk());

        verify(bookingService).approveBookingRequest(anyLong(), eq(1L), eq(true));
    }

    @Test
    public void getBookingByIdShouldReturnOk() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(new BookingsDtoBuilder().build());

        mockMvc.perform(get("/bookings/1")
                        .header(userIdHeader, 2))
                .andExpect(status().isOk());

        verify(bookingService).getBookingById(anyLong(), eq(1L));
    }

    @Test
    public void getBookingsForBookerShouldReturnList() throws Exception {
        when(bookingService.findByBooker(anyLong(), any(), any()))
                .thenReturn(Collections.singletonList(new BookingsDtoBuilder().build()));

        mockMvc.perform(get("/bookings")
                        .header(userIdHeader, 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingService).findByBooker(anyLong(), any(), any());
    }

    @Test
    public void getBookingsForOwnerShouldReturnList() throws Exception {
        when(bookingService.findByBooker(anyLong(), any(), any()))
                .thenReturn(Collections.singletonList(new BookingsDtoBuilder().build()));

        mockMvc.perform(get("/bookings/owner")
                        .header(userIdHeader, 2)
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingService).findByBooker(anyLong(), any(), any());
    }
}