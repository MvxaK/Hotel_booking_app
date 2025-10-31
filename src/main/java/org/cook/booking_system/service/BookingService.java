package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.AccommodationEntity;
import org.cook.booking_system.entity.BookingEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.mapper.BookingMapper;
import org.cook.booking_system.mapper.UserMapper;
import org.cook.booking_system.model.Accommodation;
import org.cook.booking_system.model.Booking;
import org.cook.booking_system.model.BookingRequest;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.repository.AccommodationRepository;
import org.cook.booking_system.repository.BookingRepository;
import org.cook.booking_system.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final BookingMapper bookingMapper;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AccommodationRepository accommodationRepository;

    private final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Transactional
    public Booking createBookingForUser(Long userId, BookingRequest bookingRequest){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id -> " + userId));

        AccommodationEntity accommodationEntity = accommodationRepository.findById(bookingRequest.getAccommodationId())
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found with id -> " + bookingRequest.getAccommodationId()));

        if(!isAvailable(bookingRequest.getAccommodationId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())){
            throw new IllegalArgumentException("Accommodation is not free on this date. Please try other date.");
        }

        if(bookingRequest.getCheckInDate().isAfter(bookingRequest.getCheckOutDate())){
            throw new IllegalArgumentException("Start date should be at least one day before than Checkout Date. Please try other date");
        }

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setAccommodation(accommodationEntity);
        bookingEntity.setCheckInDate(bookingRequest.getCheckInDate());
        bookingEntity.setCheckOutDate(bookingRequest.getCheckOutDate());
        bookingEntity.setStatus(Status.RESERVED);
        bookingEntity.setTotalPrice(calculateTotalPrice(bookingEntity));

        userEntity.getBookings().add(bookingEntity);
        bookingEntity.setUser(userEntity);
        logger.info("Booking with id = {} is created", bookingEntity.getId());
        return bookingMapper.toBookingModel(bookingRepository.save(bookingEntity));
    }

    @Transactional(readOnly = true)
    public Booking getBookingById(Long id){
        logger.info("Getting booking with id = {}", id);
        return bookingRepository.findById(id)
                .map(bookingMapper :: toBookingModel)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id -> " + id));
    }

    @Transactional(readOnly = true)
    public List<Booking> getAllBookingByUserId(Long userId){
        List<Booking> bookings = bookingRepository.findByUserId(userId).stream()
                .map(bookingMapper :: toBookingModel)
                .toList();

        logger.info("Getting all bookings with User.id = {}", userId);
        return bookings;
    }


    @Transactional
    public Booking updateBooking(Long id, Booking bookingToUpdate){
        BookingEntity bookingEntity = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with id -> " + id));

        if(isAvailable(bookingToUpdate.getAccommodation().getId(), bookingToUpdate.getCheckInDate(), bookingToUpdate.getCheckOutDate())){
            throw new IllegalArgumentException("Accommodation is not free on this date. Please try other date.");
        }

        if(bookingToUpdate.getStatus() != null){
            throw new IllegalArgumentException("Status should be empty");
        }

        if(!bookingEntity.getStatus().equals(Status.RESERVED)){
            throw new IllegalArgumentException("Only Reserved booking is allowed to update");
        }

        if(bookingToUpdate.getCheckInDate().isAfter(bookingToUpdate.getCheckOutDate())){
            throw new IllegalArgumentException("Start date should be at least one day before than Checkout Date. Please try other date");
        }

        bookingEntity.setStatus(Status.RESERVED);
        bookingEntity.setTotalPrice(calculateTotalPrice(bookingEntity));

        logger.info("Booking with id = {} is updated", id);
        return bookingMapper.toBookingModel(bookingRepository.save(bookingEntity));
    }


    @Transactional
    public void deleteById(Long id){
        if(!bookingRepository.existsById(id)){
            throw new EntityNotFoundException("There is no booking with id -> " + id);
        }

        BookingEntity bookingEntity = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no booking with id -> " + id));

        bookingEntity.setStatus(Status.CANCELLED);
        bookingRepository.save(bookingEntity);
    }


    private boolean isAvailable(Long accommodationId, LocalDate checkInDate, LocalDate checkOutDate){
        boolean available = true;
        List<BookingEntity> bookingEntityList = bookingRepository.findByAccommodationIdAndStatus(accommodationId, Status.RESERVED);

        for (BookingEntity booking : bookingEntityList) {
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

    @NotNull
    private BigDecimal calculateTotalPrice(@NotNull BookingEntity bookingEntity){
        int days = Math.toIntExact(ChronoUnit.DAYS.between(bookingEntity.getCheckInDate(), bookingEntity.getCheckOutDate()));

        return bookingEntity.getAccommodation().getPricePerNight().multiply(BigDecimal.valueOf(days));
    }
}
