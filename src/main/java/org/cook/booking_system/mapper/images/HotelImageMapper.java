package org.cook.booking_system.mapper.images;

import org.cook.booking_system.entity.images.HotelImageEntity;
import org.cook.booking_system.model.images.HotelImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelImageMapper {
    @Mapping(target = "hotelId", source = "hotel.id")
    HotelImage toModel(HotelImageEntity hotelImageEntity);

    @Mapping(target = "hotel", ignore = true)
    HotelImageEntity toEntity(HotelImage hotelImage);
}
