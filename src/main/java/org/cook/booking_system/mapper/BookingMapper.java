package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.BookingEntity;
import org.cook.booking_system.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class, HouseMapper.class, RoomMapper.class, AccommodationMapper.class})
public interface BookingMapper {
    @Mapping(target = "accommodation", source = "accommodation")
    Booking toBookingModel(BookingEntity bookingEntity);

    @Mapping(target = "accommodation", source = "accommodation")
    BookingEntity toBookingEntity(Booking booking);
}
