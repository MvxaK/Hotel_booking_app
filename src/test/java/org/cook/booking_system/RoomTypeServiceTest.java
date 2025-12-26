package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.booking.BookingRoomRequest;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.repository.booking.BookingRoomRepository;
import org.cook.booking_system.service.implementation.RoomServiceImpl;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
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
public class RoomTypeServiceTest {

    @Autowired
    private RoomTypeServiceImpl roomTypeService;

    @Autowired
    private HotelServiceImpl hotelService;

    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private BookingRoomServiceImpl bookingRoomService;

    @Autowired
    private BookingRoomRepository bookingRoomRepository;

    @Autowired
    private UserRepository userRepository;

    private static Long testRoomTypeId;
    private static Long testHotelId;
    private static Long testUserId;

    private RoomType roomType = new RoomType();
    private RoomType roomTypeToUpdate = new RoomType();

    @BeforeEach
    void roomTypeData() {
        roomType.setName("Amazing RoomType");
        roomType.setDescription("Amazing RoomType description");
        roomType.setPricePerNight(BigDecimal.valueOf(70000));
        roomType.setCapacity(2);
        roomType.setBedsCount(1);
        roomType.setHotelId(testHotelId);

        roomTypeToUpdate.setName("Amazing RoomType updated");
        roomTypeToUpdate.setDescription("Amazing RoomType description updated");
        roomTypeToUpdate.setPricePerNight(BigDecimal.valueOf(90000));
        roomTypeToUpdate.setCapacity(4);
        roomTypeToUpdate.setBedsCount(2);
        roomTypeToUpdate.setHotelId(testHotelId);
    }

    @Test
    @Order(1)
    void setupDependency() {
        Hotel hotel = new Hotel();
        hotel.setName("Amazing Hotel");
        hotel.setAddress("St. Somewhere 42");
        hotel.setDescription("Amazing Hotel description");

        testHotelId = hotelService.createHotel(hotel).getId();

        UserEntity user = new UserEntity();
        user.setUserName("Immortal_NoName");
        user.setEmail("immortal_noname@gmail.com");
        user.setPassword("noname");

        testUserId = userRepository.save(user).getId();

        assertNotNull(testHotelId);
        assertNotNull(testUserId);
    }

    @Test
    @Order(2)
    void createRoomType() {
        roomType.setHotelId(testHotelId);
        RoomType result = roomTypeService.createRoomTypeForHotel(roomType);

        assertNotNull(result);
        assertEquals(roomType.getName(), result.getName());

        testRoomTypeId = result.getId();
    }

    @Test
    @Order(3)
    void getRoomTypeById() {
        RoomType result = roomTypeService.getRoomTypeById(testRoomTypeId);

        assertNotNull(result);
        assertEquals(testRoomTypeId, result.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeService.getRoomTypeById(-999L);
        });
    }

    @Test
    @Order(4)
    void getRoomTypeByIdIncludeDeleted() {
        RoomType result = roomTypeService.getRoomTypeByIdIncludeDeleted(testRoomTypeId);

        assertNotNull(result);
        assertEquals(testRoomTypeId, result.getId());
    }

    @Test
    @Order(5)
    void getAllRoomTypes() {
        List<RoomType> result = roomTypeService.getAllRoomTypes();

        assertNotNull(result);
        for (RoomType roomType: result){
            assertNotNull(roomType.getId());
            assertNotNull(roomType.getHotelId());
            assertFalse(roomType.isDeleted());
        }
    }

    @Test
    @Order(6)
    void getAllIncludeDeleted() {
        List<RoomType> result = roomTypeService.getAllIncludeDeleted();

        assertNotNull(result);
        for (RoomType roomType: result){
            assertNotNull(roomType.getId());
            assertNotNull(roomType.getHotelId());
            assertNotNull(roomType.getPricePerNight());
        }
    }

    @Test
    @Order(7)
    void getRoomsByRoomTypeId() {
        List<Room> result = roomTypeService.getRoomsByRoomTypeId(testRoomTypeId);
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeService.getRoomsByRoomTypeId(-999L);
        });
    }

    @Test
    @Order(8)
    void getRoomsByRoomTypeIdDeletedTrue() {
        List<Room> result = roomTypeService.getRoomsByRoomTypeIdDeletedTrue(testRoomTypeId);
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeService.getRoomsByRoomTypeIdDeletedTrue(-999L);
        });
    }

    @Test
    @Order(9)
    void updateRoomType() {
        RoomType result = roomTypeService.updateRoomType(testRoomTypeId, roomTypeToUpdate);

        assertNotNull(result);
        assertEquals(roomTypeToUpdate.getName(), result.getName());

        hotelService.markAsDeletedHotel(testHotelId);

        assertThrows(IllegalStateException.class, () -> {
            roomTypeService.updateRoomType(testRoomTypeId, roomTypeToUpdate);
        });

        hotelService.markAsRestoredHotel(testHotelId);
    }

    @Test
    @Order(10)
    void deleteRoom_markAsDeletedRoomType_hasActiveBookings() {
        Room room = new Room();
        room.setRoomNumber("RT-101");
        room.setHotelId(testHotelId);
        room.setRoomTypeId(testRoomTypeId);
        room.setAvailable(true);

        Long testRoomId = roomService.createRoomForHotel(room).getId();

        BookingRoomRequest bookingRequest = new BookingRoomRequest();
        bookingRequest.setRoomId(testRoomId);
        bookingRequest.setCheckInDate(LocalDate.now().plusDays(1));
        bookingRequest.setCheckOutDate(LocalDate.now().plusDays(5));

        bookingRoomService.createBookingForUser(testUserId, bookingRequest);

        assertThrows(IllegalStateException.class, () -> {
            roomTypeService.markAsDeletedRoomType(testRoomTypeId);
        });

        assertThrows(IllegalStateException.class, () -> {
            roomTypeService.deleteRoomType(testRoomTypeId);
        });

        bookingRoomRepository.deleteByRoomId(testRoomId);
        roomService.deleteRoom(testRoomId);
        userRepository.deleteById(testUserId);
    }

    @Test
    @Order(11)
    void markAsDeletedRoomType() {
        roomTypeService.markAsDeletedRoomType(testRoomTypeId);

        assertThrows(IllegalStateException.class, () -> {
            roomTypeService.markAsDeletedRoomType(testRoomTypeId);
        });
    }

    @Test
    @Order(12)
    void getRoomTypeByIdDeletedTrue() {
        RoomType result = roomTypeService.getRoomTypeByIdDeletedTrue(testRoomTypeId);

        assertNotNull(result);
        assertTrue(result.isDeleted());
    }

    @Test
    @Order(13)
    void getAllRoomTypesDeletedTrue() {
        List<RoomType> result = roomTypeService.getAllRoomTypesDeletedTrue();

        assertNotNull(result);
        for (RoomType roomType: result){
            assertNotNull(roomType.getId());
            assertTrue(roomType.isDeleted());
        }
    }

    @Test
    @Order(14)
    void markAsRestoredRoomType() {
        roomTypeService.markAsRestoredRoomType(testRoomTypeId);

        assertThrows(IllegalStateException.class, () -> {
            roomTypeService.markAsRestoredRoomType(testRoomTypeId);
        });
    }

    @Test
    @Order(15)
    void deleteRoomType() {
        roomTypeService.deleteRoomType(testRoomTypeId);

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeService.getRoomTypeByIdIncludeDeleted(testRoomTypeId);
        });

        hotelService.deleteHotel(testHotelId);
    }
}