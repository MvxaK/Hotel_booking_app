package org.cook.booking_system.service.implementation;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.RoomRepository;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class AccommodationStateChanger {

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    public void deleteHotel(HotelEntity hotelEntity){
        hotelEntity.setDeleted(true);
        hotelEntity.getRoomTypes().forEach(roomType -> roomType.setDeleted(true));

        hotelEntity.getRooms().forEach(room -> {
            room.setDeleted(true);
            room.setAvailable(false);
        });

        hotelRepository.save(hotelEntity);
        roomTypeRepository.saveAll(hotelEntity.getRoomTypes());
        roomRepository.saveAll(hotelEntity.getRooms());
    }

    public void restoreHotel(HotelEntity hotelEntity){
        hotelEntity.setDeleted(false);
        hotelEntity.getRoomTypes().forEach(roomType -> roomType.setDeleted(false));

        hotelEntity.getRooms().forEach(room -> {
            room.setDeleted(false);
            room.setAvailable(true);
        });

        hotelRepository.save(hotelEntity);
        roomTypeRepository.saveAll(hotelEntity.getRoomTypes());
        roomRepository.saveAll(hotelEntity.getRooms());
    }

    public void deleteRoomType(RoomTypeEntity roomTypeEntity){
        roomTypeEntity.setDeleted(true);

        roomTypeEntity.getRooms().forEach(room -> {
            room.setDeleted(true);
            room.setAvailable(false);
        });

        roomTypeRepository.save(roomTypeEntity);
        roomRepository.saveAll(roomTypeEntity.getRooms());
    }

    public void restoreRoomType(RoomTypeEntity roomTypeEntity){
        roomTypeEntity.setDeleted(false);

        roomTypeEntity.getRooms().forEach(room -> {
            room.setDeleted(false);
            room.setAvailable(true);
        });

        roomTypeRepository.save(roomTypeEntity);
        roomRepository.saveAll(roomTypeEntity.getRooms());
    }

    public void deleteRoom(RoomEntity roomEntity){
        roomEntity.setDeleted(true);
        roomEntity.setAvailable(false);

        roomRepository.save(roomEntity);
    }

    public void restoreRoom(RoomEntity roomEntity){
        roomEntity.setDeleted(false);
        roomEntity.setAvailable(true);

        roomRepository.save(roomEntity);
    }

}
