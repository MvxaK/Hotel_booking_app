package org.cook.booking_system.mapper.booking;

import org.cook.booking_system.entity.booking.BookingRoomEntity;
import org.cook.booking_system.mapper.*;
import org.cook.booking_system.model.booking.BookingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class,RoomTypeMapper.class})
public interface BookingRoomMapper {

    @Mapping(target = "user.houseBookings", ignore = true)
    @Mapping(target = "user.roomBookings", ignore = true)
    BookingRoom toModel(BookingRoomEntity bookingRoomEntity);

    BookingRoomEntity toEntity(BookingRoom booking);
}
