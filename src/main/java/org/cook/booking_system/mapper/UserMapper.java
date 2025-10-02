package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "soring")
public interface UserMapper {
    User toUserModel(UserEntity userEntity);
    UserEntity toUserEntity(User user);
}
