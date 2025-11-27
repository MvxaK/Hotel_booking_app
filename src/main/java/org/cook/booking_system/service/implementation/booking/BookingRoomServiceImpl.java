package org.cook.booking_system.service.implementation.booking;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.RoomEntity;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.entity.booking.BookingRoomEntity;
import org.cook.booking_system.mapper.booking.BookingRoomMapper;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.model.booking.BookingRoomDetails;
import org.cook.booking_system.model.booking.BookingRoomRequest;
import org.cook.booking_system.repository.RoomRepository;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.repository.booking.BookingRoomRepository;
import org.cook.booking_system.service.service_interface.booking.BookingRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingRoomServiceImpl implements BookingRoomService{

    private final BookingRoomRepository bookingRoomRepository;
    private final BookingRoomMapper bookingRoomMapper;
    private final UserRepository userRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final Logger logger = LoggerFactory.getLogger(BookingRoomServiceImpl.class);

    @Transactional
    public BookingRoom createBookingForUser(Long userId, BookingRoomRequest bookingRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + userId));

        RoomEntity roomEntity = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id -> " + bookingRequest.getRoomId()));

        RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(roomEntity.getRoomType().getId())
                .orElseThrow(() -> new EntityNotFoundException("Room type not found with id -> " + roomEntity.getRoomType().getId()));

        if (!isAvailable(roomTypeEntity.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())) {
            throw new IllegalArgumentException("Room type not available for these dates.");
        }

        if (bookingRequest.getCheckInDate().isAfter(bookingRequest.getCheckOutDate())) {
            throw new IllegalArgumentException("Invalid date range");
        }

        BookingRoomEntity bookingEntity = new BookingRoomEntity();
        bookingEntity.setRoom(roomEntity);
        bookingEntity.setCheckInDate(bookingRequest.getCheckInDate());
        bookingEntity.setCheckOutDate(bookingRequest.getCheckOutDate());
        bookingEntity.setStatus(Status.RESERVED);
        bookingEntity.setTotalPrice(calculateTotalPrice(roomTypeEntity, bookingEntity));

        userEntity.getRoomBookings().add(bookingEntity);
        bookingEntity.setUser(userEntity);

        logger.info("Room booking created for user {}", userId);
        return bookingRoomMapper.toModel(bookingRoomRepository.save(bookingEntity));
    }

    @Transactional(readOnly = true)
    public BookingRoom getBookingById(Long id) {
        return bookingRoomRepository.findById(id).map(bookingRoomMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id -> " + id));
    }

    @Transactional(readOnly = true)
    public List<BookingRoom> getAllBookingByUserId(Long userId) {
        return bookingRoomRepository.findByUserId(userId).stream()
                .map(bookingRoomMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingRoomDetails> getAllBookingWithDetailsByUserId(Long userId){
        return bookingRoomRepository.findBookingsWithDetailsByUserId(userId);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        BookingRoomEntity booking = bookingRoomRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id -> " + bookingId));

        if (booking.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Booking is already canceled");
        }

        booking.setStatus(Status.CANCELLED);
        bookingRoomRepository.save(booking);
    }

    private boolean isAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate){
        boolean available = true;
        List<BookingRoomEntity> bookingEntityList = bookingRoomRepository.findByRoomIdAndStatus(roomId, Status.RESERVED);
        for (BookingRoomEntity booking : bookingEntityList) {
            LocalDate existingStart = booking.getCheckInDate();
            LocalDate existingEnd = booking.getCheckOutDate();

            boolean overlap = !checkOutDate.isBefore(existingStart) && !checkInDate.isAfter(existingEnd);
            if (overlap) {
                available = false;
                break;
            }
        }
        return available;
    }

    private BigDecimal calculateTotalPrice(RoomTypeEntity roomType, BookingRoomEntity booking) {
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        return roomType.getPricePerNight().multiply(BigDecimal.valueOf(days));
    }
}
