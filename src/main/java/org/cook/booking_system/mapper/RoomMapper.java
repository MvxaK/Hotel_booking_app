package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoomTypeMapper.class, HotelMapper.class})
public interface RoomMapper {

    @Mapping(target = "roomType.imagesUrl", ignore = true)
    @Mapping(target = "hotel.imagesUrl", ignore = true)
    Room toModel(RoomEntity entity);

    @Mapping(target = "hotel.images", ignore = true)
    @Mapping(target = "roomType.images", ignore = true)
    RoomEntity toEntity(Room model);
}
