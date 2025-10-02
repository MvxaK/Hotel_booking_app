package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.AccommodationEntity;
import org.cook.booking_system.model.Accommodation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccommodationMapper {
    Accommodation toAccommodationModel(AccommodationEntity accommodationEntity);
    AccommodationEntity toAccommodationEntity(Accommodation accommodation);
}
