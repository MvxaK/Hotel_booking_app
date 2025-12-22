package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.RoomServiceImpl;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomServiceTest {

    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private RoomTypeServiceImpl roomTypeService;

    @Autowired
    private HotelServiceImpl hotelService;

    private static Long testRoomId;
    private static Long testRoomTypeId;
    private static Long testHotelId;

    private Room room = new Room();
    private Room roomToUpdate = new Room();

    @BeforeEach
    void roomData() {
        room.setRoomNumber("A101");
        room.setAvailable(true);
        room.setHotelId(testHotelId);
        room.setRoomTypeId(testRoomTypeId);

        roomToUpdate.setRoomNumber("A102");
        roomToUpdate.setAvailable(false);
        roomToUpdate.setHotelId(testHotelId);
        roomToUpdate.setRoomTypeId(testRoomTypeId);
    }

    @Test
    @Order(1)
    void setupDependency() {
        Hotel hotel = new Hotel();
        hotel.setName("Amazing Hotel");
        hotel.setAddress("St. Somewhere 42");
        hotel.setDescription("Amazing Hotel description");
        Hotel createdHotel = hotelService.createHotel(hotel);
        testHotelId = createdHotel.getId();

        RoomType roomType = new RoomType();
        roomType.setName("Amazing RoomType");
        roomType.setDescription("Amazing RoomType description");
        roomType.setHotelId(testHotelId);
        roomType.setPricePerNight(BigDecimal.valueOf(70000));
        roomType.setCapacity(4);
        roomType.setBedsCount(2);

        RoomType createdType = roomTypeService.createRoomTypeForHotel(roomType);
        testRoomTypeId = createdType.getId();

        assertNotNull(testHotelId);
        assertNotNull(testRoomTypeId);
    }

    @Test
    @Order(2)
    void createRoom() {
        Room result = roomService.createRoomForHotel(room);

        assertNotNull(result);
        assertEquals(room.getRoomNumber(), result.getRoomNumber());
        assertFalse(result.isDeleted());

        testRoomId = result.getId();
    }

    @Test
    @Order(3)
    void getRoomById() {
        Room result = roomService.getRoomById(testRoomId);

        assertNotNull(result);
        assertEquals(testRoomId, result.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            roomService.getRoomById(-999L);
        });
    }

    @Test
    @Order(4)
    void getAllRooms() {
        List<Room> result = roomService.getAllRooms();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        for (Room room : result) {
            assertNotNull(room.getId());
            assertNotNull(room.getRoomNumber());
        }
    }

    @Test
    @Order(5)
    void updateRoom() {
        Room result = roomService.updateRoom(testRoomId, roomToUpdate);

        assertNotNull(result);
        assertEquals(roomToUpdate.getRoomNumber(), result.getRoomNumber());
        assertFalse(result.isAvailable());

        hotelService.markAsDeletedHotel(testHotelId);

        assertThrows(IllegalStateException.class, () -> {
            roomService.updateRoom(testRoomId, roomToUpdate);
        });

        hotelService.markAsRestoredHotel(testHotelId);
    }

    @Test
    @Order(6)
    void markAsDeletedRoom() {
        roomService.markAsDeletedRoom(testRoomId);

        assertThrows(IllegalStateException.class, () -> {
            roomService.markAsDeletedRoom(testRoomId);
        });
    }

    @Test
    @Order(7)
    void getRoomByIdDeletedTrue() {
        Room result = roomService.getRoomByIdDeletedTrue(testRoomId);

        assertNotNull(result);
        assertTrue(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            roomService.getRoomByIdDeletedTrue(-999L);
        });
    }

    @Test
    @Order(8)
    void getAllRoomsDeletedTrue() {
        List<Room> result = roomService.getAllRoomsDeletedTrue();

        assertNotNull(result);
        for (Room room: result){
            assertNotNull(room.getId());
            assertNotNull(room.getRoomTypeId());
            assertNotNull(room.getRoomNumber());
        }
    }

    @Test
    @Order(9)
    void markAsRestoredRoom() {
        roomService.markAsRestoredRoom(testRoomId);

        assertThrows(IllegalStateException.class, () -> {
            roomService.markAsRestoredRoom(testRoomId);
        });

        roomTypeService.markAsDeletedRoomType(testRoomTypeId);

        assertThrows(IllegalStateException.class, () -> {
            roomService.markAsRestoredRoom(testRoomId);
        });
    }

    @Test
    @Order(10)
    void deleteRoom() {
        roomService.deleteRoom(testRoomId);

        assertThrows(EntityNotFoundException.class, () -> {
            roomService.getRoomById(testRoomId);
        });

        roomTypeService.deleteRoomType(testRoomTypeId);
        hotelService.deleteHotel(testHotelId);
    }
}