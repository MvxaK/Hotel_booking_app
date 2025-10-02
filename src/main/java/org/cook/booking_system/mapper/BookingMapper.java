package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.BookingEntity;
import org.cook.booking_system.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBookingModel(BookingEntity bookingEntity);
    BookingEntity toBookingEntity(Booking booking);
}
