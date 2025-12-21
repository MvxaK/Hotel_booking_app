package org.cook.booking_system.mapper.images;

import org.cook.booking_system.entity.images.HotelImageEntity;
import org.cook.booking_system.model.images.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelImageMapper {

    @Mapping(target = "accommodationId", expression = "java(org.cook.booking_system.mapper.EntityIdUtils.extractId(entity.getHotel()))")
    Image toModel(HotelImageEntity entity);

    @Mapping(target = "hotel", ignore = true)
    HotelImageEntity toEntity(Image model);

}
