package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.model.booking.BookingRoomRequest;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.repository.booking.BookingRoomRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelServiceTest {

    @Autowired
    private HotelServiceImpl hotelService;

    @Autowired
    private RoomTypeServiceImpl roomTypeService;

    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private BookingRoomServiceImpl bookingRoomService;

    @Autowired
    private BookingRoomRepository bookingRoomRepository;

    @Autowired
    private UserRepository userRepository;


    private static Long testHotelId;
    private Hotel hotel = new Hotel();
    private Hotel hotelToUpdate = new Hotel();

    @BeforeEach
    void hotelData(){
        hotel.setName("Amazing hotel");
        hotel.setAddress("St. Somewhere 42");
        hotel.setDescription("Amazing description");

        hotelToUpdate.setName("Amazing hotel updated version");
        hotelToUpdate.setAddress("St. Somewhere 42");
        hotelToUpdate.setDescription("Amazing description updated version");
    }

    @Test
    @Order(1)
    void createHotel(){
        Hotel result = hotelService.createHotel(hotel);

        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getAddress());
        assertNotNull(result.getDescription());
        assertFalse(result.isDeleted());

        testHotelId = result.getId();
    }

    @Test
    @Order(2)
    void getHotelById(){
        Hotel result = hotelService.getHotelById(testHotelId);

        assertNotNull(result);
        assertEquals(testHotelId, result.getId());
        assertFalse(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            Hotel hotel = hotelService.getHotelById(-999L);
        });
    }

    @Test
    @Order(3)
    void getHotelByIdIncludeDeleted(){
        Hotel result = hotelService.getHotelByIdIncludeDeleted(testHotelId);

        assertNotNull(result);
        assertEquals(testHotelId, result.getId());
    }

    @Test
    @Order(4)
    void getAllHotels(){
        List<Hotel> result = hotelService.getAllHotels();

        assertNotNull(result);
        for (Hotel hotel: result){
            assertNotNull(hotel.getId());
            assertNotNull(hotel.getDescription());
            assertFalse(hotel.isDeleted());
        }
    }

    @Test
    @Order(5)
    void getRoomsByHotelId(){
        List<Room> result = hotelService.getRoomsByHotelId(testHotelId);

        assertNotNull(result);
        for (Room room: result){
            assertNotNull(room.getId());
            assertNotNull(room.getRoomTypeId());
            assertFalse(room.isDeleted());
        }

        assertThrows(EntityNotFoundException.class, () -> {
            List<Room> rooms = hotelService.getRoomsByHotelId(-999L);
        });
    }

    @Test
    @Order(6)
    void getRoomsByHotelIdDeletedTrue(){
        List<Room> result = hotelService.getRoomsByHotelIdDeletedTrue(testHotelId);

        assertNotNull(result);
        for (Room room: result){
            assertNotNull(room.getId());
            assertNotNull(room.getRoomTypeId());
            assertTrue(room.isDeleted());
        }

        assertThrows(EntityNotFoundException.class, () -> {
            List<Room> rooms = hotelService.getRoomsByHotelIdDeletedTrue(-999L);
        });
    }

    @Test
    @Order(7)
    void getRoomTypesByHotelId(){
        List<RoomType> result = hotelService.getRoomTypesByHotelId(testHotelId);

        assertNotNull(result);
        for (RoomType roomType: result){
            assertNotNull(roomType.getId());
            assertNotNull(roomType.getHotelId());
            assertNotNull(roomType.getName());
            assertNotNull(roomType.getPricePerNight());
            assertFalse(roomType.isDeleted());
        }

        assertThrows(EntityNotFoundException.class, () -> {
            List<RoomType> roomTypes = hotelService.getRoomTypesByHotelId(-999L);
        });
    }

    @Test
    @Order(8)
    void getRoomTypesByHotelIdDeletedTrue(){
        List<RoomType> result = hotelService.getRoomTypesByHotelIdDeletedTrue(testHotelId);

        assertNotNull(result);
        for (RoomType roomType: result){
            assertNotNull(roomType.getId());
            assertNotNull(roomType.getHotelId());
            assertNotNull(roomType.getName());
            assertNotNull(roomType.getPricePerNight());
            assertTrue(roomType.isDeleted());
        }

        assertThrows(EntityNotFoundException.class, () -> {
            List<RoomType> roomTypes = hotelService.getRoomTypesByHotelIdDeletedTrue(-999L);
        });
    }

    @Test
    @Order(9)
    void updateHotel(){
        Hotel result = hotelService.updateHotel(testHotelId, hotelToUpdate);

        assertNotNull(result);
        assertEquals(testHotelId, result.getId());
        assertEquals(hotelToUpdate.getName(), result.getName());
        assertEquals(hotelToUpdate.getDescription(), result.getDescription());

        assertThrows(EntityNotFoundException.class, () -> {
            hotelService.updateHotel(-999L, hotelToUpdate);
        });


        hotelService.markAsDeletedHotel(testHotelId);

        assertThrows(IllegalStateException.class, () -> {
            hotelService.updateHotel(testHotelId, hotelToUpdate);
        });

        hotelService.markAsRestoredHotel(testHotelId);
    }

    @Test
    @Order(10)
    void deleteHotel_markAsDeletedHotel_hasActiveBookings() {
        UserEntity user = new UserEntity();
        user.setUserName("Immortal_NoName");
        user.setEmail("immortal_noname@gmail.com");
        user.setPassword("noname");

        Long testUserId = userRepository.save(user).getId();

        RoomType roomType = new RoomType();
        roomType.setName("Amazing RoomType");
        roomType.setDescription("Amazing RoomType description");
        roomType.setPricePerNight(BigDecimal.valueOf(100000));
        roomType.setCapacity(2);
        roomType.setBedsCount(1);
        roomType.setHotelId(testHotelId);

        Long testRoomTypeId = roomTypeService.createRoomTypeForHotel(roomType).getId();

        Room room = new Room();
        room.setRoomNumber("A101");
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
            hotelService.markAsDeletedHotel(testHotelId);
        });

        assertThrows(IllegalStateException.class, () -> {
            hotelService.deleteHotel(testHotelId);
        });

        bookingRoomRepository.deleteByRoomId(testRoomId);
        roomService.deleteRoom(testRoomId);
        roomTypeService.deleteRoomType(testRoomTypeId);
        userRepository.deleteById(testUserId);
    }

    @Test
    @Order(11)
    void markAsDeletedHotel() {
        hotelService.markAsDeletedHotel(testHotelId);

        assertThrows(IllegalStateException.class, () -> {
            hotelService.markAsDeletedHotel(testHotelId);
        });
    }

    @Test
    @Order(12)
    void getHotelByIdDeletedTrue(){
        Hotel result = hotelService.getHotelByIdDeletedTrue(testHotelId);

        assertNotNull(result);
        assertEquals(testHotelId, result.getId());
        assertTrue(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            Hotel hotel = hotelService.getHotelByIdDeletedTrue(-999L);
        });
    }

    @Test
    @Order(13)
    void getAllHotelsDeletedTrue(){
        List<Hotel> result = hotelService.getAllHotelsDeletedTrue();

        assertNotNull(result);
        for (Hotel hotel: result){
            assertNotNull(hotel.getId());
            assertNotNull(hotel.getDescription());
            assertTrue(hotel.isDeleted());
        }
    }

    @Test
    @Order(14)
    void markAsRestoredHotel() {
        hotelService.markAsRestoredHotel(testHotelId);

        assertThrows(IllegalStateException.class, () -> {
            hotelService.markAsRestoredHotel(testHotelId);
        });
    }

    @Test
    @Order(15)
    void deleteHotel() {
        hotelService.deleteHotel(testHotelId);

        assertThrows(EntityNotFoundException.class, () -> {
            hotelService.deleteHotel(testHotelId);
        });
    }

}
