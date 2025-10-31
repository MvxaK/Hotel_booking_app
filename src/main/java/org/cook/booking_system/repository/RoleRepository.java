package org.cook.booking_system.repository;

import org.cook.booking_system.entity.RoleEntity;
import org.cook.booking_system.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRole(Role role);
}
