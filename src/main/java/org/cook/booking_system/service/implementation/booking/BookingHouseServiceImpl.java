package org.cook.booking_system.service.implementation.booking;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.entity.booking.BookingHouseEntity;
import org.cook.booking_system.mapper.booking.BookingHouseMapper;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.booking.BookingHouse;
import org.cook.booking_system.model.booking.BookingHouseDetails;
import org.cook.booking_system.model.booking.BookingHouseRequest;
import org.cook.booking_system.repository.HouseRepository;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.repository.booking.BookingHouseRepository;
import org.cook.booking_system.service.service_interface.booking.BookingHouseService;
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
public class BookingHouseServiceImpl implements BookingHouseService{

    private final BookingHouseRepository bookingHouseRepository;
    private final BookingHouseMapper bookingHouseMapper;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    @Transactional
    public BookingHouse createBookingForUser(Long userId, BookingHouseRequest bookingRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + userId));

        HouseEntity houseEntity = houseRepository.findById(bookingRequest.getHouseId())
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + bookingRequest.getHouseId()));

        if (!isAvailable(houseEntity.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())) {
            throw new IllegalArgumentException("House not available for these dates");
        }

        BookingHouseEntity bookingEntity = new BookingHouseEntity();
        bookingEntity.setHouse(houseEntity);
        bookingEntity.setCheckInDate(bookingRequest.getCheckInDate());
        bookingEntity.setCheckOutDate(bookingRequest.getCheckOutDate());
        bookingEntity.setStatus(Status.RESERVED);
        bookingEntity.setTotalPrice(calculateTotalPrice(houseEntity, bookingEntity));

        userEntity.getHouseBookings().add(bookingEntity);
        bookingEntity.setUser(userEntity);

        return bookingHouseMapper.toModel(bookingHouseRepository.save(bookingEntity));
    }

    @Transactional(readOnly = true)
    public BookingHouse getBookingById(Long id) {
        return bookingHouseRepository.findById(id).map(bookingHouseMapper::toModel)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + id));
    }

    @Transactional(readOnly = true)
    public List<BookingHouse> getAllBookingByUserId(Long userId) {
        return bookingHouseRepository.findByUserId(userId).stream()
                .map(bookingHouseMapper::toModel).toList();
    }

    @Transactional(readOnly = true)
    public List<BookingHouseDetails> getAllBookingWithDetailsByUserId(Long userId){
        return bookingHouseRepository.findBookingDetailsByUserId(userId);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        BookingHouseEntity booking = bookingHouseRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id -> " + bookingId));

        if (booking.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Booking is already canceled");
        }

        booking.setStatus(Status.CANCELLED);
        bookingHouseRepository.save(booking);
    }

    private boolean isAvailable(Long houseId, LocalDate checkInDate, LocalDate checkOutDate){
        boolean available = true;
        List<BookingHouseEntity> bookingEntityList = bookingHouseRepository.findByHouseIdAndStatus(houseId, Status.RESERVED);
        for (BookingHouseEntity booking : bookingEntityList) {
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

    private BigDecimal calculateTotalPrice(HouseEntity house, BookingHouseEntity booking) {
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());

        return house.getPricePerNight().multiply(BigDecimal.valueOf(days));
    }
}
