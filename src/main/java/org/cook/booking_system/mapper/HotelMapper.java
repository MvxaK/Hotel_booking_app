package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.mapper.images.HotelImageMapper;
import org.cook.booking_system.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {HotelImageMapper.class})
public interface HotelMapper {

    @Mapping(target = "images", source = "entity.images")
    @Mapping(target = "roomIds", expression = "java(EntityIdUtils.extractIds(entity.getRooms()))")
    @Mapping(target = "roomTypeIds", expression = "java(EntityIdUtils.extractIds(entity.getRoomTypes()))")
    Hotel toModel(HotelEntity entity);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    HotelEntity toEntity(Hotel model);


}