package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.mapper.images.HouseImageMapper;
import org.cook.booking_system.model.House;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {HouseImageMapper.class})
public interface HouseMapper {

    @Mapping(target = "images", source = "entity.images")
    House toModel(HouseEntity entity);

    @Mapping(target = "images", ignore = true)
    HouseEntity toEntity(House model);

}
