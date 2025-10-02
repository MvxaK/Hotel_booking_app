package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.model.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    Hotel toHotelModel(HotelEntity hotelEntity);
    HotelEntity toHotelEntity(Hotel hotel);
}
