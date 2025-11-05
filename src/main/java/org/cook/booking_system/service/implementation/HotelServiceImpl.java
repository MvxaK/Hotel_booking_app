package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.mapper.EntityLinker;
import org.cook.booking_system.mapper.HotelMapper;
import org.cook.booking_system.mapper.RoomMapper;
import org.cook.booking_system.mapper.RoomTypeMapper;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final EntityLinker entityLinker;
    private final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);

    @Transactional
    public Hotel createHotel(Hotel hotel) {
        HotelEntity hotelEntity = hotelMapper.toEntity(hotel);

        entityLinker.linkHotel(hotelEntity, hotel.getRoomIds(), hotel.getRoomTypeIds());

        HotelEntity saved = hotelRepository.save(hotelEntity);
        logger.info("Hotel with id = {} is created", saved.getId());
        return hotelMapper.toModel(saved);
    }

    @Transactional(readOnly = true)
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .map(hotelMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));
    }

    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByHotelId(Long id) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        return hotelEntity.getRooms().stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomType> getRoomTypesByHotelId(Long id) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        return hotelEntity.getRoomTypes().stream()
                .map(roomTypeMapper::toModel)
                .toList();
    }

    @Transactional
    public Hotel updateHotel(Long id, Hotel hotelToUpdate) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        hotelEntity.setName(hotelToUpdate.getName());
        hotelEntity.setAddress(hotelToUpdate.getAddress());
        hotelEntity.setDescription(hotelToUpdate.getDescription());

        entityLinker.linkHotel(hotelEntity, hotelToUpdate.getRoomIds(), hotelToUpdate.getRoomTypeIds());

        HotelEntity saved = hotelRepository.save(hotelEntity);
        logger.info("Hotel with id = {} is updated", id);
        return hotelMapper.toModel(saved);
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new EntityNotFoundException("Hotel not found with id -> " + id);
        }

        logger.info("Hotel with id = {} is deleted", id);
        hotelRepository.deleteById(id);
    }
}
