package org.cook.booking_system.mapper.booking;

import org.cook.booking_system.entity.booking.BookingRoomEntity;
import org.cook.booking_system.mapper.EntityIdUtils;
import org.cook.booking_system.model.booking.BookingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = EntityIdUtils.class)
public interface BookingRoomMapper {

    @Mapping(target = "userId", expression = "java(EntityIdUtils.extractId(entity.getUser()))")
    @Mapping(target = "roomTypeId", expression = "java(EntityIdUtils.extractId(entity.getRoom().getRoomType()))")
    BookingRoom toModel(BookingRoomEntity entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "room", ignore = true)
    BookingRoomEntity toEntity(BookingRoom booking);
}