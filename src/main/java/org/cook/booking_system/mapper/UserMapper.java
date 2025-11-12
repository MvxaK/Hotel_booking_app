package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "houseBookingIds", expression = "java(EntityIdUtils.extractIds(entity.getHouseBookings()))")
    @Mapping(target = "roomBookingIds", expression = "java(EntityIdUtils.extractIds(entity.getRoomBookings()))")
    User toModel(UserEntity entity);

    @Mapping(target = "houseBookings", ignore = true)
    @Mapping(target = "roomBookings", ignore = true)
    UserEntity toEntity(User user);
}
