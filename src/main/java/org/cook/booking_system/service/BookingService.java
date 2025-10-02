package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.BookingEntity;
import org.cook.booking_system.mapper.BookingMapper;
import org.cook.booking_system.mapper.UserMapper;
import org.cook.booking_system.model.Accommodation;
import org.cook.booking_system.model.Booking;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.User;
import org.cook.booking_system.repository.BookingRepository;
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
public class BookingService {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final BookingMapper bookingMapper;

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper, UserService userService, UserMapper userMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Transactional
    public Booking createbooking(Booking booking){
        if(isAvailable(booking.getAccommodation(), booking.getCheckInDate(), booking.getCheckOutDate())){
            throw new IllegalArgumentException("Accommodation is not free on this date. Please try other date.");
        }

        if(booking.getStatus() != null){
            throw new IllegalArgumentException("Status should be empty");
        }

        if(booking.getCheckInDate().isAfter(booking.getCheckOutDate())){
            throw new IllegalArgumentException("Start date should be at least one day before than Checkout Date. Please try other date");
        }


        BookingEntity bookingEntity = bookingMapper.toBookingEntity(booking);
        bookingEntity.setStatus(Status.RESERVED);
        bookingEntity.setTotalPrice(calculateTotalPrice(bookingEntity));


        logger.info("Booking with id = {} is created", booking.getId());
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
    public List<Booking> getGetAllBookingByUser(User user){
        List<Booking> bookings = bookingRepository.findByUserId(user.getId()).stream()
                .map(bookingMapper :: toBookingModel)
                .toList();

        logger.info("Getting all bookings with User.id = {}", user.getId());
        return bookings;
    }


    @Transactional
    public Booking updateBooking(Long id, Booking bookingToUpdate){
        BookingEntity bookingEntity = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with id -> " + id));

        if(isAvailable(bookingToUpdate.getAccommodation(), bookingToUpdate.getCheckInDate(), bookingToUpdate.getCheckOutDate())){
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


    private boolean isAvailable(@NotNull Accommodation accommodation, LocalDate checkInDate, LocalDate checkOutDate){
        boolean available = true;
        List<BookingEntity> bookingEntityList = bookingRepository.findByAccommodationIdAndStatusIn(accommodation.getId(), Status.RESERVED);

        for (BookingEntity booking : bookingEntityList){
            if(booking.getCheckInDate().isBefore(checkInDate) && booking.getCheckOutDate().isAfter(checkOutDate)){
                available = false;
                break;
            }
            if(booking.getCheckInDate().isBefore(checkInDate) && booking.getCheckOutDate().isBefore(checkOutDate)){
                available = false;
                break;
            }
            if(booking.getCheckInDate().isAfter(checkInDate) && booking.getCheckOutDate().isBefore(checkOutDate)){
                available = false;
                break;
            }
            if(booking.getCheckInDate().isAfter(checkInDate) && booking.getCheckOutDate().isAfter(checkOutDate)){
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
