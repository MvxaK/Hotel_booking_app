package org.cook.booking_system.repository;

import org.cook.booking_system.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    @Override
    @Query("select r from RoomEntity r where r.deleted = false")
    List<RoomEntity> findAll();

    Optional<RoomEntity> findByIdAndDeletedFalse(Long id);

    Optional<RoomEntity> findByIdAndDeletedTrue(Long id);

    List<RoomEntity> findByHotelIdAndDeletedFalse(Long hotelId);

    List<RoomEntity> findByRoomTypeIdAndDeletedFalse(Long roomTypeId);

    List<RoomEntity> findByHotelIdAndDeletedTrue(Long hotelId);

    List<RoomEntity> findByDeletedTrue();
}
