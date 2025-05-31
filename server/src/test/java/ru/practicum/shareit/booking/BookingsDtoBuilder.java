package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

class BookingsDtoBuilder {
    public BookingDto build() {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusDays(1));
        dto.setStatus(BookingStatus.WAITING);
        return dto;
    }
}