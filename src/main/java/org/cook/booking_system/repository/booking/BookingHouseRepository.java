package org.cook.booking_system.repository.booking;

import org.cook.booking_system.entity.booking.BookingHouseEntity;
import org.cook.booking_system.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHouseRepository extends JpaRepository<BookingHouseEntity, Long> {
    List<BookingHouseEntity> findByUserId(Long userId);
    List<BookingHouseEntity> findByHouseIdAndStatus(Long houseId, Status status);
}
