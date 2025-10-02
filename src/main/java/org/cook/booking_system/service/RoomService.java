package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
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
public class RoomService {

    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final RoomMapper roomMapper;

    private final Logger logger = LoggerFactory.getLogger(RoomService.class);

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Transactional
    public Room createRoom(Room room){
        RoomEntity entity = roomMapper.toRoomEntity(room);
        RoomEntity saved = roomRepository.save(entity);
        logger.info("Room with id -> {} is created", room.getId());
        return roomMapper.toRoomModel(saved);
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms(){
        return roomRepository.findAll().stream()
                .map(roomMapper::toRoomModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long id){
        return roomRepository.findById(id)
                .map(roomMapper::toRoomModel)
                .orElseThrow( ()-> new EntityNotFoundException("Room not found with id -> " + id));
    }

    @Transactional
    public Room updateRoom(Long id, Room roomToUpdate){
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow( ()->new EntityNotFoundException("Room not found with id -> " + id));

        roomEntity.setRoomNumber(roomToUpdate.getRoomNumber());
        roomEntity.setHotel(roomToUpdate.getHotel());
        roomEntity.setCapacity(roomToUpdate.getCapacity());
        roomEntity.setPricePerNight(roomToUpdate.getPricePerNight());
        roomEntity.setAvailable(roomToUpdate.isAvailable());
        roomEntity.setImages(roomToUpdate.getImages());

        logger.info("Room with id -> {} is updated", id);
        return roomMapper.toRoomModel(roomRepository.save(roomEntity));
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
