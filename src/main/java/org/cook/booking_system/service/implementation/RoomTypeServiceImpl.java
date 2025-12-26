package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.mapper.RoomMapper;
import org.cook.booking_system.mapper.RoomTypeMapper;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.repository.RoomRepository;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.repository.booking.BookingRoomRepository;
import org.cook.booking_system.service.service_interface.RoomTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomMapper roomMapper;
    private final AccommodationLinker accommodationLinker;
    private final AccommodationStateChanger accommodationStateChanger;

    @Transactional(readOnly = true)
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll().stream()
                .map(roomTypeMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomType> getAllRoomTypesDeletedTrue() {
        return roomTypeRepository.findByDeletedTrue().stream()
                .map(roomTypeMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomType> getAllIncludeDeleted() {
        return roomTypeRepository.findAllIncludeDeleted().stream()
                .map(roomTypeMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findByIdAndDeletedFalse(id)
                .map(roomTypeMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found or marked as deleted with id -> " + id));
    }

    @Transactional(readOnly = true)
    public RoomType getRoomTypeByIdDeletedTrue(Long id) {
        return roomTypeRepository.findByIdAndDeletedTrue(id)
                .map(roomTypeMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Deleted RoomType not found -> " + id));
    }

    @Transactional(readOnly = true)
    public RoomType getRoomTypeByIdIncludeDeleted(Long id) {
        return roomTypeRepository.findById(id)
                .map(roomTypeMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found -> " + id));
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByRoomTypeId(Long id) {
        if (!roomTypeRepository.findByIdAndDeletedFalse(id).isPresent()) {
            throw new EntityNotFoundException("RoomType not found or marked as deleted with id -> " + id);
        }

        return roomRepository.findByRoomTypeIdAndDeletedFalse(id).stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByRoomTypeIdDeletedTrue(Long id) {
        if (!roomTypeRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("RoomType not found with id -> " + id);
        }

        return roomRepository.findByRoomTypeIdAndDeletedTrue(id).stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional
    public RoomType createRoomTypeForHotel(RoomType roomType) {
        RoomTypeEntity roomTypeEntity = roomTypeMapper.toEntity(roomType);

        roomTypeEntity.setDeleted(false);

        accommodationLinker.linkRoomType(roomTypeEntity, roomType.getHotelId(), roomType.getRoomIds());

        return roomTypeMapper.toModel(roomTypeRepository.save(roomTypeEntity));
    }

    @Transactional
    public RoomType updateRoomType(Long id, RoomType updated) {
        RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found with id -> " + id));

        if(roomTypeEntity.isDeleted() || roomTypeEntity.getHotel().isDeleted()){
            throw new IllegalStateException("Cannot update roomType that is marked as deleted or with Hotel that marked as deleted");
        }

        roomTypeEntity.setName(updated.getName());
        roomTypeEntity.setDescription(updated.getDescription());
        roomTypeEntity.setPricePerNight(updated.getPricePerNight());
        roomTypeEntity.setCapacity(updated.getCapacity());
        roomTypeEntity.setBedsCount(updated.getBedsCount());

        accommodationLinker.linkRoomType(roomTypeEntity, updated.getHotelId(), updated.getRoomIds());

        return roomTypeMapper.toModel(roomTypeRepository.save(roomTypeEntity));
    }

    @Transactional
    public void deleteRoomType(Long id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new EntityNotFoundException("RoomType not found -> " + id);
        }

        boolean hasActiveBookings = bookingRoomRepository.existsActiveBookingsByRoomTypeId(id, LocalDate.now(), Status.RESERVED);
        if(hasActiveBookings){
            throw new IllegalStateException("Cannot delete room type with active or future bookings associated with its rooms.");
        }

        bookingRoomRepository.deleteByRoomTypeId(id);
        roomTypeRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeletedRoomType(Long id){
        RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found -> " + id));

        if(roomTypeEntity.isDeleted()){
            throw new IllegalStateException("Cannot delete roomType that already marked as deleted");
        }

        boolean hasActiveBookings = bookingRoomRepository.existsActiveBookingsByRoomTypeId(id, LocalDate.now(), Status.RESERVED);
        if(hasActiveBookings){
            throw new IllegalStateException("Cannot delete room type with active or future bookings associated with its rooms.");
        }

        accommodationStateChanger.deleteRoomType(roomTypeEntity);
    }

    @Transactional
    public void markAsRestoredRoomType(Long id){
        RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found -> " + id));

        if (!roomTypeEntity.isDeleted()) {
            throw new IllegalStateException("Cannot restore roomType that already restored");
        }

        if(roomTypeEntity.getHotel().isDeleted()){
            throw new IllegalStateException("Cannot restore roomType with Hotel that marked as deleted");
        }

        accommodationStateChanger.restoreRoomType(roomTypeEntity);
    }

}

