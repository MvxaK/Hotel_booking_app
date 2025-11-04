package org.cook.booking_system.repository.images;

import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeImageRepository extends JpaRepository<RoomTypeImageEntity, Long> {
    List<RoomTypeImageEntity> findByRoomTypeId(Long roomTypeId);
}
