package org.cook.booking_system.mapper.booking;

import org.cook.booking_system.entity.booking.BookingHouseEntity;
import org.cook.booking_system.mapper.HouseMapper;
import org.cook.booking_system.mapper.RoleMapper;
import org.cook.booking_system.mapper.UserMapper;
import org.cook.booking_system.model.booking.BookingHouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class, HouseMapper.class})
public interface BookingHouseMapper {

    @Mapping(target = "user.houseBookings", ignore = true)
    @Mapping(target = "user.roomBookings", ignore = true)
    BookingHouse toModel(BookingHouseEntity bookingHouseEntity);

    BookingHouseEntity toEntity(BookingHouse booking);
}
