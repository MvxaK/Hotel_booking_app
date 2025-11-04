package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.entity.images.HouseImageEntity;
import org.cook.booking_system.model.House;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HouseMapper {
    @Mapping(target = "imagesUrl", expression = "java(mapImages(entity.getImages()))")
    House toModel(HouseEntity entity);

    @Mapping(target = "images", ignore = true)
    HouseEntity toEntity(House model);

    default List<String> mapImages(List<HouseImageEntity> images) {
        if (images == null)
            return List.of();

        return images.stream()
                .map(HouseImageEntity::getUrl)
                .toList();
    }

}
