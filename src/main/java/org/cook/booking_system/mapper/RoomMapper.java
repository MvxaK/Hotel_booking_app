package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.model.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toRoomModel(RoomEntity roomEntity);
    RoomEntity toRoomEntity(Room room);
}
