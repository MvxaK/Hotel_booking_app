package org.cook.booking_system.repository.booking;

import org.cook.booking_system.entity.booking.BookingRoomEntity;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.booking.BookingRoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoomEntity, Long> {
    List<BookingRoomEntity> findByUserId(Long userId);
    List<BookingRoomEntity> findByRoomIdAndStatus(Long roomId, Status status);

    @Query("select new org.cook.booking_system.model.booking.BookingRoomDetails(b.id, b.user.id, r.roomNumber, rt.name, ht.name, b.checkInDate, b.checkOutDate, b.totalPrice, b.status) from BookingRoomEntity b join b.room r join r.roomType rt join r.hotel ht where b.user.id = :userId")
    List<BookingRoomDetails> findBookingsWithDetailsByUserId(@Param("userId") Long userId);
}
