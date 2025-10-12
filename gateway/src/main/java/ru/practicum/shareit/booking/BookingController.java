package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final String userIdHeader = "X-Sharer-User-Id";
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> createBookingRequest(@RequestHeader(userIdHeader) Long userId,
												@Valid @RequestBody BookingDto newBookingDto) {
		return bookingClient.createBookingRequest(userId, newBookingDto);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsForBooker(@RequestHeader(userIdHeader) Long userId,
														  @RequestParam(name = "state", defaultValue = "all")
														  String stateParam,
														  @PositiveOrZero
														  @RequestParam(name = "from", defaultValue = "0")
														  Integer from,
														  @Positive
														  @RequestParam(name = "size", defaultValue = "10")
														  Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Неверный state: " + stateParam));
		return bookingClient.getBookingsForBooker(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsForOwner(@RequestHeader(userIdHeader) Long userId,
														 @RequestParam(defaultValue = "ALL") BookingState state) {
		return bookingClient.getBookingsForOwner(userId, state);
	}


	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader(userIdHeader) Long userId,
												 @PathVariable("bookingId") Long bookingId) {
		return bookingClient.getBookingById(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBookingRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
													  @PathVariable("bookingId") Long bookingId,
													  @RequestParam(name = "approved", required = true) Boolean approved) {
		return bookingClient.approveBookingRequest(userId, bookingId, approved);
	}
}
