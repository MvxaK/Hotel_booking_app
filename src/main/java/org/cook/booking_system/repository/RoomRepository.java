package org.cook.booking_system.repository;

import org.cook.booking_system.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findByHotelId(Long hotelId);
}
