package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.RoomRepository;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class AccommodationLinker {

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;


    public void linkHotel(HotelEntity hotelEntity, List<Long> roomIds, List<Long> roomTypeIds) {
        linkHotelToRooms(hotelEntity, roomIds);
        linkHotelToRoomTypes(hotelEntity, roomTypeIds);
    }

    public void linkRoomType(RoomTypeEntity roomTypeEntity, Long hotelId, List<Long> roomIds) {
        linkRoomTypeToHotel(roomTypeEntity, hotelId);
        linkRoomTypeToRooms(roomTypeEntity, roomIds);
    }

    public void linkRoom(RoomEntity roomEntity, Long hotelId, Long roomTypeId) {
        linkRoomToHotel(roomEntity, hotelId);
        linkRoomToRoomType(roomEntity, roomTypeId);
    }


    private void linkHotelToRoomTypes(HotelEntity hotelEntity, List<Long> roomTypeIds) {
        if (roomTypeIds != null && !roomTypeIds.isEmpty()) {
            List<RoomTypeEntity> roomTypes = roomTypeRepository.findAllById(roomTypeIds);

            for (RoomTypeEntity roomType : roomTypes) {
                roomType.setHotel(hotelEntity);
            }
            hotelEntity.setRoomTypes(roomTypes);
        }
    }

    private void linkHotelToRooms(HotelEntity hotelEntity, List<Long> roomIds) {
        if (roomIds != null && !roomIds.isEmpty()) {
            List<RoomEntity> rooms = roomRepository.findAllById(roomIds);

            for (RoomEntity room : rooms) {
                room.setHotel(hotelEntity);
            }
            hotelEntity.setRooms(rooms);
        }
    }

    private void linkRoomToRoomType(RoomEntity roomEntity, Long roomTypeId) {
        if (roomTypeId != null) {
            RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no roomType with id -> " + roomTypeId));

            if(!roomType.getHotel().getId().equals(roomEntity.getHotel().getId())){
                throw new IllegalArgumentException("RoomType not belongs to entered hotel");
            }

            roomEntity.setRoomType(roomType);
        }
    }

    private void linkRoomToHotel(RoomEntity roomEntity, Long hotelId) {
        if (hotelId != null) {
            HotelEntity hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no hotel with id -> " + hotelId));

            if(hotel.isDeleted()){
                throw new IllegalStateException("Cannot create room with for Hotel that marked as deleted.");
            }

            roomEntity.setHotel(hotel);
            hotel.getRooms().add(roomEntity);
        }
    }

    private void linkRoomTypeToRooms(RoomTypeEntity roomTypeEntity, List<Long> roomIds) {
        if (roomIds != null && !roomIds.isEmpty()) {
            List<RoomEntity> rooms = roomRepository.findAllById(roomIds);

            for (RoomEntity room : rooms) {
                room.setRoomType(roomTypeEntity);
            }

            roomTypeEntity.setRooms(rooms);
        }
    }

    private void linkRoomTypeToHotel(RoomTypeEntity roomTypeEntity, Long hotelId) {
        if (hotelId != null) {
            HotelEntity hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no hotel with id -> " + hotelId));

            if (hotel.isDeleted()) {
                throw new IllegalStateException("Cannot link RoomType to a deleted Hotel");
            }

            roomTypeEntity.setHotel(hotel);
        }
    }
}