package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.mapper.HotelMapper;
import org.cook.booking_system.mapper.RoomMapper;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    private final HotelRepository hotelRepository;

    @Autowired
    private final HotelMapper hotelMapper;

    @Autowired
    private final RoomMapper roomMapper;


    private final Logger logger = LoggerFactory.getLogger(HotelService.class);


    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper, RoomMapper roomMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
        this.roomMapper = roomMapper;
    }

    @Transactional
    public Hotel createHotel(Hotel hotel){
        HotelEntity hotelEntity = hotelMapper.toHotelEntity(hotel);
        hotelRepository.save(hotelEntity);

        logger.info("Hotel with id = {} is created", hotel.getId());
        return hotelMapper.toHotelModel(hotelEntity);
    }

    @Transactional(readOnly = true)
    public Hotel getHotelById(Long id){
        return hotelRepository.findById(id)
                .map(hotelMapper :: toHotelModel)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));
    }

    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels(){
        return hotelRepository.findAll().stream()
                .map(hotelMapper :: toHotelModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByHotelId(Long id){
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        return hotelEntity.getRooms().stream()
                .map(roomMapper :: toRoomModel)
                .toList();
    }

    @Transactional
    public Hotel updateHotel(Long id, Hotel hotelToUpdate){
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        hotelEntity.setName(hotelToUpdate.getName());
        hotelEntity.setAddress(hotelToUpdate.getAddress());
        hotelEntity.setRooms(hotelToUpdate.getRooms());
        hotelEntity.setImages(hotelToUpdate.getImages());

        logger.info("Hotel with id = {} is updated", id);
        return hotelMapper.toHotelModel(hotelRepository.save(hotelEntity));
    }

    @Transactional
    public void deleteHotel(Long id){
        if(!hotelRepository.existsById(id)){
            throw new EntityNotFoundException("Hotel not found with id -> " + id);
        }

        logger.info("Hotel with id = {} is deleted", id);
        hotelRepository.deleteById(id);
    }
}
