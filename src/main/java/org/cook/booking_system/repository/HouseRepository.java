package org.cook.booking_system.repository;

import org.cook.booking_system.entity.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<HouseEntity, Long> {

    @Override
    @Query("select h from HouseEntity h where h.deleted = false")
    List<HouseEntity> findAll();

    Optional<HouseEntity> findByIdAndDeletedFalse(Long id);

    Optional<HouseEntity> findByIdAndDeletedTrue(Long id);

    List<HouseEntity> findByDeletedTrue();

}
