package org.cook.booking_system.mapper;

import org.cook.booking_system.entity.AccommodationEntity;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.model.Accommodation;
import org.cook.booking_system.model.House;
import org.cook.booking_system.model.Room;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, HouseMapper.class})
public abstract class AccommodationMapper {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private HouseMapper houseMapper;

    public Accommodation toAccommodationModel(AccommodationEntity entity) {
        if (entity == null) return null;

        if (entity instanceof RoomEntity roomEntity) {
            return roomMapper.toRoomModel(roomEntity);
        } else if (entity instanceof HouseEntity houseEntity) {
            return houseMapper.toHouseModel(houseEntity);
        }

        throw new IllegalArgumentException("Unknown accommodation type: " + entity.getClass());
    }

    public AccommodationEntity toAccommodationEntity(Accommodation model) {
        if (model == null) return null;

        if (model instanceof Room room) {
            return roomMapper.toRoomEntity(room);
        } else if (model instanceof House house) {
            return houseMapper.toHouseEntity(house);
        }

        throw new IllegalArgumentException("Unknown accommodation model type: " + model.getClass());
    }
}