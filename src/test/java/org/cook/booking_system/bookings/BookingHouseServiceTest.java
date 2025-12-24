package org.cook.booking_system.bookings;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.House;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.booking.BookingHouse;
import org.cook.booking_system.model.booking.BookingHouseRequest;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
import org.cook.booking_system.service.implementation.booking.BookingHouseServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingHouseServiceTest {

    @Autowired
    private BookingHouseServiceImpl bookingHouseService;

    @Autowired
    private HouseServiceImpl houseService;

    @Autowired
    private UserRepository userRepository;

    private static Long testBookingId;
    private static Long testUserId;
    private static Long testHouseId;

    private BookingHouseRequest bookingRequest = new BookingHouseRequest();

    @BeforeEach
    void bookingData() {
        bookingRequest.setHouseId(testHouseId);
        bookingRequest.setCheckInDate(LocalDate.now().plusDays(1));
        bookingRequest.setCheckOutDate(LocalDate.now().plusDays(5));
    }

    @Test
    @Order(1)
    void setupDependencies() {
        UserEntity user = new UserEntity();
        user.setUserName("Immortal_NoName");
        user.setEmail("immortal_noname@gmail.com");
        user.setPassword("noname");

        testUserId = userRepository.save(user).getId();

        House house = new House();
        house.setName("Amazing House");
        house.setLocation("St. Something 42");
        house.setCapacity(10);
        house.setPricePerNight(BigDecimal.valueOf(100000));
        house.setAvailable(true);
        house.setRoomsNumber(4);
        house.setBedsCount(6);
        house.setParkingSlots(4);
        house.setDescription("Amazing description");

        testHouseId = houseService.createHouse(house).getId();

        assertNotNull(testUserId);
        assertNotNull(testHouseId);
    }

    @Test
    @Order(2)
    void createBooking() {
        houseService.markAsDeletedHouse(testHouseId);
        assertThrows(EntityNotFoundException.class, () -> {
            bookingHouseService.createBookingForUser(testUserId, bookingRequest);
        });
        houseService.markAsRestoredHouse(testHouseId);

        BookingHouse result = bookingHouseService.createBookingForUser(testUserId, bookingRequest);

        assertNotNull(result);

        testBookingId = result.getId();

        assertEquals(testHouseId, result.getHouseId());
        assertEquals(Status.RESERVED, result.getStatus());
        assertEquals(0, BigDecimal.valueOf(400000).compareTo(result.getTotalPrice()));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingHouseService.createBookingForUser(testUserId, bookingRequest);
        });

    }

    @Test
    @Order(3)
    void getBookingById() {
        BookingHouse result = bookingHouseService.getBookingById(testBookingId);

        assertNotNull(result);
        assertEquals(testBookingId, result.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            bookingHouseService.getBookingById(-999L);
        });
    }

    @Test
    @Order(4)
    void getAllBookingByUserId() {
        List<BookingHouse> result = bookingHouseService.getAllBookingByUserId(testUserId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(testUserId, result.get(0).getUserId());
    }

    @Test
    @Order(5)
    void cancelBooking() {
        bookingHouseService.cancelBooking(testBookingId);

        BookingHouse result = bookingHouseService.getBookingById(testBookingId);
        assertEquals(Status.CANCELLED, result.getStatus());

        assertThrows(IllegalStateException.class, () -> {
            bookingHouseService.cancelBooking(testBookingId);
        });
    }

    @Test
    @Order(6)
    void deleteBooking() {
        bookingHouseService.deleteBooking(testBookingId);

        assertThrows(EntityNotFoundException.class, () -> {
            bookingHouseService.getBookingById(testBookingId);
        });

        assertThrows(EntityNotFoundException.class, () -> {
            bookingHouseService.deleteBooking(testBookingId);
        });

        houseService.deleteHouse(testHouseId);
        userRepository.deleteById(testUserId);
    }
}