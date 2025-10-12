package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBookingRequest(@RequestHeader(userIdHeader) long userId,
                                           @RequestBody BookingRequest bookingRequest) {
        return bookingService.createBookingRequest(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBookingRequest(@RequestHeader(userIdHeader) long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam Boolean approved) {
        return bookingService.approveBookingRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(userIdHeader) long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsForBooker(@RequestHeader(userIdHeader) Long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state,
                                                 @PageableDefault(sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.findByBooker(userId, state, pageable);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestHeader(userIdHeader) Long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state,
                                                @PageableDefault(sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.findByBooker(userId, state, pageable);
    }
}
