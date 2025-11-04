package org.cook.booking_system.repository.booking;

import org.cook.booking_system.entity.booking.BookingRoomEntity;
import org.cook.booking_system.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoomEntity, Long> {
    List<BookingRoomEntity> findByUserId(Long userId);
    List<BookingRoomEntity> findByRoomIdAndStatus(Long roomId, Status status);
}
