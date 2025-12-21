package org.cook.booking_system.repository;

import org.cook.booking_system.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    @Override
    @Query("select h from HotelEntity h where h.deleted = false")
    List<HotelEntity> findAll();

    Optional<HotelEntity> findByIdAndDeletedFalse(Long id);

    Optional<HotelEntity> findByIdAndDeletedTrue(Long id);

    List<HotelEntity> findByDeletedTrue();

}
