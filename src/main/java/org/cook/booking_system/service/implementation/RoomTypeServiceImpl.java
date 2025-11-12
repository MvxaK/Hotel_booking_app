package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.mapper.EntityLinker;
import org.cook.booking_system.mapper.RoomMapper;
import org.cook.booking_system.mapper.RoomTypeMapper;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.service.service_interface.RoomTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomMapper roomMapper;
    private final EntityLinker entityLinker;

    @Transactional(readOnly = true)
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll().stream()
                .map(roomTypeMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id)
                .map(roomTypeMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found -> " + id));
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByRoomTypeId(Long id) {
        RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found -> " + id));

        return roomTypeEntity.getRooms().stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional
    public RoomType create(RoomType roomType) {
        RoomTypeEntity entity = roomTypeMapper.toEntity(roomType);

        entityLinker.linkRoomType(entity, roomType.getHotelId(), roomType.getRoomIds());

        RoomTypeEntity saved = roomTypeRepository.save(entity);
        return roomTypeMapper.toModel(saved);
    }

    @Transactional
    public RoomType update(Long id, RoomType updated) {
        RoomTypeEntity entity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found with id -> " + id));

        entity.setName(updated.getName());
        entity.setDescription(updated.getDescription());
        entity.setPricePerNight(updated.getPricePerNight());
        entity.setCapacity(updated.getCapacity());
        entity.setBedsCount(updated.getBedsCount());

        entityLinker.linkRoomType(entity, updated.getHotelId(), updated.getRoomIds());

        RoomTypeEntity saved = roomTypeRepository.save(entity);
        return roomTypeMapper.toModel(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new EntityNotFoundException("RoomType not found -> " + id);
        }

        roomTypeRepository.deleteById(id);
    }
}
