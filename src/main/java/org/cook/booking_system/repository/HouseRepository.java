package org.cook.booking_system.repository;

import org.cook.booking_system.entity.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<HouseEntity, Long> {

}
