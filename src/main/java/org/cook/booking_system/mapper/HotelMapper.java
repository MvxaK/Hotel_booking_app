package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.images.HotelImageEntity;
import org.cook.booking_system.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "imagesUrl", expression = "java(mapImages(entity.getImages()))")
    @Mapping(target = "roomIds", expression = "java(EntityIdUtils.extractIds(entity.getRooms()))")
    @Mapping(target = "roomTypeIds", expression = "java(EntityIdUtils.extractIds(entity.getRoomTypes()))")
    Hotel toModel(HotelEntity entity);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    HotelEntity toEntity(Hotel model);

    default List<String> mapImages(List<HotelImageEntity> images) {
        if (images == null)
            return List.of();

        return images.stream()
                .map(HotelImageEntity::getUrl)
                .toList();
    }
}