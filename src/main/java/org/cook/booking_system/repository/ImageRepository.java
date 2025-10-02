package org.cook.booking_system.repository;

import org.cook.booking_system.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByHotelId(Long hotelId);
    List<ImageEntity> findByAccommodationId(Long accommodationId);
}
