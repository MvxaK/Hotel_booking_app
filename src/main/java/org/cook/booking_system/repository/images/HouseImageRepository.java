package org.cook.booking_system.repository.images;

import org.cook.booking_system.entity.images.HouseImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseImageRepository extends JpaRepository<HouseImageEntity, Long> {
    List<HouseImageEntity> findByHouseId(Long houseId);

    boolean existsByIdAndHouseId(Long id, Long houseId);
}
