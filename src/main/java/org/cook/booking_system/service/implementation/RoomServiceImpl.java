package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.mapper.RoomMapper;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.RoomRepository;
import org.cook.booking_system.repository.booking.BookingRoomRepository;
import org.cook.booking_system.service.service_interface.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final RoomMapper roomMapper;
    private final AccommodationLinker accommodationLinker;
    private final AccommodationStateChanger accommodationStateChanger;

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRoomsDeletedTrue() {
        return roomRepository.findByDeletedTrue().stream()
                .map(roomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long id) {
        return roomRepository.findByIdAndDeletedFalse(id)
                .map(roomMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Room not found or marked as deleted with id -> " + id));
    }

    @Transactional(readOnly = true)
    public Room getRoomByIdDeletedTrue(Long id) {
        return roomRepository.findByIdAndDeletedTrue(id)
                .map(roomMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Deleted Room not found with id -> " + id));
    }

    @Transactional
    public Room createRoomForHotel(Room room) {
        RoomEntity roomEntity = roomMapper.toEntity(room);

        roomEntity.setDeleted(false);

        accommodationLinker.linkRoom(roomEntity, room.getHotelId(), room.getRoomTypeId());

        return roomMapper.toModel(roomRepository.save(roomEntity));
    }

    @Transactional
    public Room updateRoom(Long id, Room roomToUpdate) {
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + id));

        if(roomEntity.isDeleted() || roomEntity.getHotel().isDeleted()){
            throw new IllegalStateException("Cannot update room that is marked as deleted or with Hotel that marked as deleted");
        }

        roomEntity.setRoomNumber(roomToUpdate.getRoomNumber());
        roomEntity.setAvailable(roomToUpdate.isAvailable());

        accommodationLinker.linkRoom(roomEntity, roomToUpdate.getHotelId(), roomToUpdate.getRoomTypeId());

        return roomMapper.toModel(roomRepository.save(roomEntity));
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found with id -> " + id);
        }

        boolean hasActiveBookings = bookingRoomRepository.existsActiveBookingsByRoomId(id, LocalDate.now(), Status.RESERVED);
        if (hasActiveBookings) {
            throw new IllegalStateException("Cannot delete room with active or future bookings.");
        }

        bookingRoomRepository.deleteByRoomId(id);
        roomRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeletedRoom(Long id) {
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + id));

        if(roomEntity.isDeleted()){
            throw new IllegalStateException("Cannot delete room that already marked as deleted");
        }

        boolean hasActiveBookings = bookingRoomRepository.existsActiveBookingsByRoomId(id, LocalDate.now(), Status.RESERVED);
        if (hasActiveBookings) {
            throw new IllegalStateException("Cannot delete room with active or future bookings.");
        }

        accommodationStateChanger.deleteRoom(roomEntity);
    }

    @Transactional
    public void markAsRestoredRoom(Long id) {
        RoomEntity roomEntity = roomRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + id));

        if(!roomEntity.isDeleted()){
            throw new IllegalStateException("Cannot restore room that already restored");
        }

        if(roomEntity.getRoomType().isDeleted()){
            throw new IllegalStateException("Cannot restore room with RoomType that marked as deleted");
        }

        if(roomEntity.getHotel().isDeleted()){
            throw new IllegalStateException("Cannot restore room with Hotel that marked as deleted");
        }

        accommodationStateChanger.restoreRoom(roomEntity);
    }

}
