package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.ItemUnavailableException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto createBookingRequest(long userId, BookingRequest bookingRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User не был найден"));
        Item item = itemRepository.findById(bookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Item не был найден"));

        if (!item.getAvailable()) {
            throw new ItemUnavailableException("Item сейчас не доступен для бронирования");
        }

        Booking booking = bookingRepository.save(BookingMapper.bookingRequestToBooking(bookingRequest, item, user));
        return BookingMapper.buildBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto approveBookingRequest(long userId, long bookingId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не может быть найдено"));

        if (!userRepository.existsById(userId)) {
            throw new NullPointerException("Пользователь не найден");
        }

        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new NotFoundException("Пользователь не является владельцем Item");
        }

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(
                    () -> new NotFoundException(String.format("Item c id:%s не был найден", booking.getItem().getId()))
                );

        LocalDateTime endTime = bookingRepository.findLastBookingEndDateByItemId(item.getId());

        if (isApproved) {
            item.setAvailable(false);

            if (endTime != null) {
                item.setEndBooking(endTime);
            }

            item.setStartBooking(booking.getStartTime());
            booking.setStatus(BookingStatus.APPROVED);

        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);

        return BookingMapper.buildBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NullPointerException("Такого бронирования нет"));

        if (!booking.getItem().getOwnerId().equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Переданный пользователь не является владельцем вещи или автором бронирования");
        }

        return BookingMapper.buildBookingDto(booking);
    }

    @Override
    public List<BookingDto> findByBooker(Long userId, BookingState state, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NullPointerException("Пользователь не найден");
        }
        LocalDateTime now = LocalDateTime.now();
        List<Booking> list = switch (state) {
            case CURRENT  -> bookingRepository.findByBookerIdAndStartTimeBeforeAndEndTimeAfter(userId, now, now, pageable);
            case PAST     -> bookingRepository.findByBookerIdAndEndTimeIsBefore(userId, now, pageable);
            case FUTURE   -> bookingRepository.findByBookerIdAndStartTimeIsAfter(userId, now, pageable);
            case WAITING  -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, pageable);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, pageable);
            case ALL      -> bookingRepository.findByBookerId(userId, pageable);
        };
        return list.stream()
                .map(BookingMapper::buildBookingDto)
                .collect(Collectors.toList());
    };
}
