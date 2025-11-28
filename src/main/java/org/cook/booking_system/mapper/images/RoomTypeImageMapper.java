package org.cook.booking_system.mapper.images;

import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.cook.booking_system.model.images.RoomTypeImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomTypeImageMapper {

    @Mapping(target = "roomTypeId", source = "roomType.id")
    RoomTypeImage toModel(RoomTypeImageEntity entity);

    @Mapping(target = "roomType", ignore = true)
    RoomTypeImageEntity toEntity(RoomTypeImage model);

}
