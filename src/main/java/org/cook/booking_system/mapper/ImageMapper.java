package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.ImageEntity;
import org.cook.booking_system.model.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    Image toImageModel(ImageEntity imageEntity);
    ImageEntity toImageEntity(Image image);
}
