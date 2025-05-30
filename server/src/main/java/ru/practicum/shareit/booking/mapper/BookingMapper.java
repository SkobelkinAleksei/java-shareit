package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public static BookingDto buildBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStartTime());
        bookingDto.setEnd(booking.getEndTime());
        bookingDto.setItem(ItemMapper.buildItemDto(booking.getItem()));
        bookingDto.setBooker(UserMapper.buildToUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking bookingRequestToBooking(BookingRequest bookingRequest, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStartTime(bookingRequest.getStart());
        booking.setEndTime(bookingRequest.getEnd());
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        return booking;
    }
}
