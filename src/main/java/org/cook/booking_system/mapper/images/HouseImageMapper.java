package org.cook.booking_system.mapper.images;

import org.cook.booking_system.entity.images.HouseImageEntity;
import org.cook.booking_system.model.images.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HouseImageMapper {

    @Mapping(target = "accommodationId", expression = "java(org.cook.booking_system.mapper.EntityIdUtils.extractId(entity.getHouse()))")
    Image toModel(HouseImageEntity entity);

    @Mapping(target = "house", ignore = true)
    HouseImageEntity toEntity(Image model);

}
