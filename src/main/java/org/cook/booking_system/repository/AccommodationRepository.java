package org.cook.booking_system.repository;

import org.cook.booking_system.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {

}
