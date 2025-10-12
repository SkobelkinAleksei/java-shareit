package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingRequest;

import java.util.List;

public interface BookingService {
    BookingDto createBookingRequest(long userId, BookingRequest bookingRequest);

    BookingDto approveBookingRequest(long userId, long bookingId, boolean isApproved);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> findByBooker(Long userId, BookingState state, Pageable pageable);
}
