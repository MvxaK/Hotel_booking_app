package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.mapper.HotelMapper;
import org.cook.booking_system.mapper.RoomTypeMapper;
import org.cook.booking_system.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.mapper.RoomMapper;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;
    private final HotelMapper hotelMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final Logger logger = LoggerFactory.getLogger(RoomService.class);

    @Transactional
    public Room createRoomForHotel(Long hotelId, Room room){
        HotelEntity hotelEntity = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + hotelId));

        RoomEntity roomEntity = roomMapper.toEntity(room);
        hotelEntity.getRooms().add(roomEntity);

        logger.info("Room with id -> {} is created", room.getId());
        return roomMapper.toModel(roomRepository.save(roomEntity));
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms(){
        return roomRepository.findAll().stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long id){
        return roomRepository.findById(id)
                .map(roomMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + id));
    }

    @Transactional
    public Room updateRoom(Long id, Room roomToUpdate){
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + id));

        HotelEntity hotelEntity = hotelMapper.toEntity(roomToUpdate.getHotel());
        RoomTypeEntity roomTypeEntity = roomTypeMapper.toEntity(roomToUpdate.getRoomType());

        roomEntity.setRoomNumber(roomToUpdate.getRoomNumber());
        roomEntity.setAvailable(roomToUpdate.isAvailable());
        roomEntity.setHotel(hotelEntity);
        roomEntity.setRoomType(roomTypeEntity);

        logger.info("Room with id -> {} is updated", id);
        return roomMapper.toModel(roomRepository.save(roomEntity));
    }

    @Transactional
    public void deleteRoom(Long id){
        if(!roomRepository.existsById(id)){
            throw new EntityNotFoundException("Room not found with id -> " + id);
        }

        logger.info("Room with id -> {} is deleted", id);
        roomRepository.deleteById(id);
    }
}
