package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    User toModel(UserEntity userEntity);
    UserEntity toEntity(User user);
}
