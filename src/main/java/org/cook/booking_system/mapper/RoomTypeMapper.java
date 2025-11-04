package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.cook.booking_system.model.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    @Mapping(target = "imagesUrl", expression = "java(mapImages(entity.getImages()))")
    RoomType toModel(RoomTypeEntity entity);

    @Mapping(target = "images", ignore = true)
    RoomTypeEntity toEntity(RoomType model);

    default List<String> mapImages(List<RoomTypeImageEntity> images) {
        if (images == null)
            return List.of();

        return images.stream()
                .map(RoomTypeImageEntity::getUrl)
                .toList();
    }

}
