package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.mapper.EntityIdUtils;
import org.cook.booking_system.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "hotelId", expression = "java(EntityIdUtils.extractId(entity.getHotel()))")
    @Mapping(target = "roomTypeId", expression = "java(EntityIdUtils.extractId(entity.getRoomType()))")
    Room toModel(RoomEntity entity);

    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "roomType", ignore = true)
    RoomEntity toEntity(Room model);
}