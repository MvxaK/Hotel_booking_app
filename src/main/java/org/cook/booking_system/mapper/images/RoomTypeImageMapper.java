package org.cook.booking_system.mapper.images;

import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.cook.booking_system.model.images.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomTypeImageMapper {

    @Mapping(target = "accommodationId", expression = "java(org.cook.booking_system.mapper.EntityIdUtils.extractId(entity.getRoomType()))")
    Image toModel(RoomTypeImageEntity entity);

    @Mapping(target = "roomType", ignore = true)
    RoomTypeImageEntity toEntity(Image model);

}
