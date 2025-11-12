package org.cook.booking_system.mapper.booking;

import org.cook.booking_system.entity.booking.BookingHouseEntity;
import org.cook.booking_system.mapper.EntityIdUtils;
import org.cook.booking_system.model.booking.BookingHouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = EntityIdUtils.class)
public interface BookingHouseMapper {

    @Mapping(target = "userId", expression = "java(EntityIdUtils.extractId(entity.getUser()))")
    @Mapping(target = "houseId", expression = "java(EntityIdUtils.extractId(entity.getHouse()))")
    BookingHouse toModel(BookingHouseEntity entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "house", ignore = true)
    BookingHouseEntity toEntity(BookingHouse booking);
}