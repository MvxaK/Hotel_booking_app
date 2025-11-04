package org.cook.booking_system.repository.images;

import org.cook.booking_system.entity.images.HotelImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelImageRepository extends JpaRepository<HotelImageEntity, Long> {
    List<HotelImageEntity> findByHotelId(Long hotelId);
}
