package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.RoleEntity;
import org.cook.booking_system.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default Role toRoleModel(RoleEntity roleEntity){
        if(roleEntity == null)
            return null;

        return roleEntity.getRole();
    }

    default RoleEntity toRoleEntity(Role role){
        if(role == null)
            return null;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(role);

        return roleEntity;
    }
}
