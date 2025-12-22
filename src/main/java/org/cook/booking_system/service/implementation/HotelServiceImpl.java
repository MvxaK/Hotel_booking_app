package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.mapper.*;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.RoomRepository;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.repository.booking.BookingRoomRepository;
import org.cook.booking_system.service.service_interface.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final AccommodationLinker accommodationLinker;
    private final AccommodationStateChanger accommodationStateChanger;

    @Transactional(readOnly = true)
    public Hotel getHotelById(Long id) {
        return hotelRepository.findByIdAndDeletedFalse(id)
                .map(hotelMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found or marked as deleted with id -> " + id));
    }

    @Transactional(readOnly = true)
    public Hotel getHotelByIdDeletedTrue(Long id) {
        return hotelRepository.findByIdAndDeletedTrue(id)
                .map(hotelMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Deleted Hotel not found with id -> " + id));
    }

    @Transactional(readOnly = true)
    public Hotel getHotelByIdIncludeDeleted(Long id) {
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
    public List<Hotel> getAllHotelsDeletedTrue() {
        return hotelRepository.findByDeletedTrue().stream()
                .map(hotelMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByHotelId(Long id) {
        if (!hotelRepository.findByIdAndDeletedFalse(id).isPresent()) {
            throw new EntityNotFoundException("Hotel not found or marked as deleted with id -> " + id);
        }

        return roomRepository.findByHotelIdAndDeletedFalse(id).stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByHotelIdDeletedTrue(Long id) {
        if (!hotelRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("Hotel not found with id -> " + id);
        }

        return roomRepository.findByHotelIdAndDeletedTrue(id).stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomType> getRoomTypesByHotelId(Long id) {
        if (!hotelRepository.findByIdAndDeletedFalse(id).isPresent()) {
            throw new EntityNotFoundException("Hotel not found or marked as deleted with id -> " + id);
        }

        return roomTypeRepository.findByHotelIdAndDeletedFalse(id).stream()
                .map(roomTypeMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomType> getRoomTypesByHotelIdDeletedTrue(Long id) {
        if (!hotelRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("Hotel not found with id -> " + id);
        }

        return roomTypeRepository.findByHotelIdAndDeletedTrue(id).stream()
                .map(roomTypeMapper::toModel)
                .toList();
    }

    @Transactional
    public Hotel createHotel(Hotel hotel) {
        HotelEntity hotelEntity = hotelMapper.toEntity(hotel);
        hotelEntity.setDeleted(false);

        accommodationLinker.linkHotel(hotelEntity, hotel.getRoomIds(), hotel.getRoomTypeIds());

        HotelEntity saved = hotelRepository.save(hotelEntity);
        return hotelMapper.toModel(saved);
    }

    @Transactional
    public Hotel updateHotel(Long id, Hotel hotelToUpdate) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        if(hotelEntity.isDeleted()){
            throw new IllegalStateException("Cannot update hotel that already marked as deleted");
        }

        hotelEntity.setName(hotelToUpdate.getName());
        hotelEntity.setAddress(hotelToUpdate.getAddress());
        hotelEntity.setDescription(hotelToUpdate.getDescription());

        accommodationLinker.linkHotel(hotelEntity, hotelToUpdate.getRoomIds(), hotelToUpdate.getRoomTypeIds());

        HotelEntity saved = hotelRepository.save(hotelEntity);
        return hotelMapper.toModel(saved);
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new EntityNotFoundException("Hotel not found with id -> " + id);
        }

        boolean hasActiveBookings = bookingRoomRepository.existsActiveBookingsByHotelId(id, LocalDate.now(), Status.RESERVED);
        if(hasActiveBookings){
            throw new IllegalStateException("Cannot delete Hotel with active or future bookings");
        }

        bookingRoomRepository.deleteByHotelId(id);
        hotelRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeletedHotel(Long id){
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        if(hotelEntity.isDeleted()){
            throw new IllegalStateException("Cannot delete hotel that already marked as deleted");
        }

        boolean hasActiveBookings = bookingRoomRepository.existsActiveBookingsByHotelId(id, LocalDate.now(), Status.RESERVED);
        if(hasActiveBookings){
            throw new IllegalStateException("Cannot delete Hotel with active or future bookings");
        }

        accommodationStateChanger.deleteHotel(hotelEntity);
    }

    @Transactional
    public void markAsRestoredHotel(Long id){
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + id));

        if(!hotelEntity.isDeleted()){
            throw new IllegalStateException("Cannot restore hotel that already restored");
        }

        accommodationStateChanger.restoreHotel(hotelEntity);
    }

}
