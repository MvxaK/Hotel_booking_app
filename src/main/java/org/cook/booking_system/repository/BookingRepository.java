package org.cook.booking_system.repository;

import org.cook.booking_system.entity.BookingEntity;
import org.cook.booking_system.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByUserId(Long userId);
    List<BookingEntity> findByStatus(Status status);
    List<BookingEntity> findByAccommodationId(Long accommodationId);

    List<BookingEntity> findByAccommodationIdAndStatus(Long id, Status status);
}
