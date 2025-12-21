package org.cook.booking_system.repository;

import org.cook.booking_system.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Long> {

    @Override
    @Query("select rt from RoomTypeEntity rt where rt.deleted = false")
    List<RoomTypeEntity> findAll();

    Optional<RoomTypeEntity> findByIdAndDeletedFalse(Long id);

    Optional<RoomTypeEntity> findByIdAndDeletedTrue(Long id);

    List<RoomTypeEntity> findByHotelIdAndDeletedFalse(Long hotelId);

    List<RoomTypeEntity> findByHotelIdAndDeletedTrue(Long hotelId);

    List<RoomTypeEntity> findByDeletedTrue();

    @Query("select rt from RoomTypeEntity rt")
    List<RoomTypeEntity> findAllIncludeDeleted();
}
