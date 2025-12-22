package org.cook.booking_system.bookings;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.model.booking.BookingRoomRequest;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.RoomServiceImpl;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.cook.booking_system.service.implementation.booking.BookingRoomServiceImpl;
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
public class BookingRoomServiceTest {

    @Autowired
    private BookingRoomServiceImpl bookingRoomService;

    @Autowired
    private HotelServiceImpl hotelService;

    @Autowired
    private RoomTypeServiceImpl roomTypeService;

    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private UserRepository userRepository;

    private static Long testBookingId;
    private static Long testUserId;
    private static Long testHotelId;
    private static Long testRoomTypeId;
    private static Long testRoomId;

    private BookingRoomRequest bookingRequest = new BookingRoomRequest();

    @BeforeEach
    void bookingData() {
        bookingRequest.setRoomId(testRoomId);
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

        UserEntity savedUser = userRepository.save(user);
        testUserId = savedUser.getId();

        Hotel hotel = new Hotel();
        hotel.setName("Amazing Hotel");
        hotel.setAddress("St. Somewhere 42");
        hotel.setDescription("Amazing Hotel description");

        Hotel createdHotel = hotelService.createHotel(hotel);
        testHotelId = createdHotel.getId();

        RoomType roomType = new RoomType();
        roomType.setName("Amazing RoomType");
        roomType.setDescription("Amazing RoomType description");
        roomType.setPricePerNight(BigDecimal.valueOf(100000));
        roomType.setCapacity(2);
        roomType.setBedsCount(1);
        roomType.setHotelId(testHotelId);

        RoomType createdType = roomTypeService.createRoomTypeForHotel(roomType);
        testRoomTypeId = createdType.getId();

        Room room = new Room();
        room.setRoomNumber("A101");
        room.setAvailable(true);
        room.setHotelId(testHotelId);
        room.setRoomTypeId(testRoomTypeId);

        Room createRoom = roomService.createRoomForHotel(room);
        testRoomId = createRoom.getId();

        assertNotNull(testUserId);
        assertNotNull(testRoomId);
    }

    @Test
    @Order(2)
    void createBooking() {
        roomService.markAsDeletedRoom(testRoomId);
        assertThrows(EntityNotFoundException.class, () -> {
            bookingRoomService.createBookingForUser(testUserId, bookingRequest);
        });
        roomService.markAsRestoredRoom(testRoomId);

        BookingRoom result = bookingRoomService.createBookingForUser(testUserId, bookingRequest);

        assertNotNull(result);

        testBookingId = result.getId();

        assertEquals(testRoomId, result.getRoomId());
        assertEquals(Status.RESERVED, result.getStatus());
        assertEquals(0, BigDecimal.valueOf(400000).compareTo(result.getTotalPrice()));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingRoomService.createBookingForUser(testUserId, bookingRequest);
        });
    }

    @Test
    @Order(3)
    void getBookingById() {
        BookingRoom result = bookingRoomService.getBookingById(testBookingId);

        assertNotNull(result);
        assertEquals(testBookingId, result.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            bookingRoomService.getBookingById(-999L);
        });
    }

    @Test
    @Order(4)
    void getAllBookingByUserId() {
        List<BookingRoom> result = bookingRoomService.getAllBookingByUserId(testUserId);

        assertNotNull(result);
        for (BookingRoom bookingRoom: result){
            assertNotNull(bookingRoom.getId());
            assertNotNull(bookingRoom.getCheckInDate());
            assertNotNull(bookingRoom.getCheckOutDate());
            assertNotNull(bookingRoom.getTotalPrice());
        }
    }

    @Test
    @Order(5)
    void cancelBooking() {
        bookingRoomService.cancelBooking(testBookingId);

        BookingRoom result = bookingRoomService.getBookingById(testBookingId);
        assertEquals(Status.CANCELLED, result.getStatus());

        assertThrows(IllegalStateException.class, () -> {
            bookingRoomService.cancelBooking(testBookingId);
        });
    }

    @Test
    @Order(6)
    void deleteBooking() {
        bookingRoomService.deleteBooking(testBookingId);

        assertThrows(EntityNotFoundException.class, () -> {
            bookingRoomService.getBookingById(testBookingId);
        });

        roomService.deleteRoom(testRoomId);
        roomTypeService.deleteRoomType(testRoomTypeId);
        hotelService.deleteHotel(testHotelId);
        userRepository.deleteById(testUserId);
    }
}