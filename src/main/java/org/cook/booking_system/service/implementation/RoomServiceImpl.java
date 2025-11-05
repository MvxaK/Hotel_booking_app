package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.mapper.EntityLinker;
import org.cook.booking_system.mapper.RoomMapper;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.RoomRepository;
import org.cook.booking_system.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;
    private final EntityLinker entityLinker;
    private final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Transactional
    public Room createRoomForHotel(Long hotelId, Room room) {
        HotelEntity hotelEntity = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + hotelId));

        RoomEntity roomEntity = roomMapper.toEntity(room);

        entityLinker.linkRoom(roomEntity, hotelId, room.getRoomTypeId());

        hotelEntity.getRooms().add(roomEntity);

        RoomEntity saved = roomRepository.save(roomEntity);
        logger.info("Room with id -> {} is created", saved.getId());
        return roomMapper.toModel(saved);
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .map(roomMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + id));
    }

    @Transactional
    public Room updateRoom(Long id, Room roomToUpdate) {
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + id));

        roomEntity.setRoomNumber(roomToUpdate.getRoomNumber());
        roomEntity.setAvailable(roomToUpdate.isAvailable());

        entityLinker.linkRoom(roomEntity, roomToUpdate.getHotelId(), roomToUpdate.getRoomTypeId());

        RoomEntity saved = roomRepository.save(roomEntity);
        logger.info("Room with id -> {} is updated", id);
        return roomMapper.toModel(saved);
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found with id -> " + id);
        }

        logger.info("Room with id -> {} is deleted", id);
        roomRepository.deleteById(id);
    }
}
