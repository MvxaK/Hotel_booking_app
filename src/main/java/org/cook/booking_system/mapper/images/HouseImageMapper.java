package org.cook.booking_system.mapper.images;

import org.cook.booking_system.entity.images.HouseImageEntity;
import org.cook.booking_system.model.images.HouseImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HouseImageMapper {

    @Mapping(target = "houseId", source = "house.id")
    HouseImage toModel(HouseImageEntity houseImageEntity);

    @Mapping(target = "house", ignore = true)
    HouseImageEntity toEntity(HouseImage houseImage);

}
