package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.mapper.images.RoomTypeImageMapper;
import org.cook.booking_system.model.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {RoomTypeImageMapper.class})
public interface RoomTypeMapper {

    @Mapping(target = "images", source = "entity.images")
    @Mapping(target = "hotelId", expression = "java(EntityIdUtils.extractId(entity.getHotel()))")
    @Mapping(target = "roomIds", expression = "java(EntityIdUtils.extractIds(entity.getRooms()))")
    RoomType toModel(RoomTypeEntity entity);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    RoomTypeEntity toEntity(RoomType model);

}