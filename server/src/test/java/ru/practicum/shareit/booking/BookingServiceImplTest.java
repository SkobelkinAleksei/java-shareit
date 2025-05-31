package ru.practicum.shareit.booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;

    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwnerId(2L);

        booking = new Booking();
        booking.setId(1L);
        booking.setStartTime(LocalDateTime.now().plusDays(1));
        booking.setEndTime(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    public void createBookingRequestShouldCreateAndReturnDto() {
        BookingRequest request = new BookingRequest();
        request.setItemId(item.getId());
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto result = bookingService.createBookingRequest(user.getId(), request);

        assertNotNull(result);
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking savedBooking = bookingCaptor.getValue();
        assertEquals(BookingStatus.WAITING, savedBooking.getStatus());
    }

    @Test
    public void approveBookingRequestShouldApproveAndUpdateItem() {
        long userId = 2L;
        long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime lastEndDate = LocalDateTime.now().minusDays(1);

        when(bookingRepository.findLastBookingEndDateByItemId(item.getId()))
                .thenReturn(lastEndDate);

        BookingDto result = bookingService.approveBookingRequest(userId, bookingId, true);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
        assertFalse(item.getAvailable());
    }

    @Test
    public void getBookingByIdShouldReturnDtoWhenUserIsOwnerOrBooker() {
        long userId = 2L;
        long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDto dto = bookingService.getBookingById(userId, bookingId);

        assertNotNull(dto);
    }

    @Test
    public void findByBookerShouldReturnListBasedOnState() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        List<Booking> bookings = Collections.singletonList(booking);

        when(bookingRepository.findByBookerIdAndStartTimeBeforeAndEndTimeAfter(eq(userId), any(), any(), any()))
                .thenReturn(bookings);

        for (BookingState state : BookingState.values()) {
            List<BookingDto> dtos = bookingService.findByBooker(userId, state, PageRequest.of(0, 10));
            assertNotNull(dtos);
        }
    }
}