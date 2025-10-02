package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.model.House;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HouseMapper {
    House toHouseModel(HouseEntity houseEntity);
    HouseEntity toHouseEntity(House house);
}
