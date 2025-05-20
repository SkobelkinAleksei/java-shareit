package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStartTimeBeforeAndEndTimeAfter(Long bookerId, LocalDateTime now1, LocalDateTime now2, Pageable pageable);

    List<Booking> findByBookerIdAndEndTimeIsBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStartTimeIsAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    @Query("""
            SELECT MAX(b.endTime)
            FROM Booking b
            WHERE b.item.id = :itemId
           """)
    LocalDateTime findLastBookingEndDateByItemId(@Param("itemId") Long itemId);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId AND i.id = :itemId AND b.status = 'APPROVED' AND b.endTime < :currentTime")
    List<Booking> getAllUserBookings(Long bookerId, Long itemId, LocalDateTime currentTime);
}

