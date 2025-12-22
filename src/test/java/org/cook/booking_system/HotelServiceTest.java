package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelServiceTest {

    @Autowired
    private HotelServiceImpl hotelService;

    private static Long hotelId;
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

        hotelId = result.getId();
    }

    @Test
    @Order(2)
    void getHotelById(){
        Hotel result = hotelService.getHotelById(hotelId);

        assertNotNull(result);
        assertEquals(hotelId, result.getId());
        assertFalse(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            Hotel hotel = hotelService.getHotelById(-999L);
        });
    }

    @Test
    @Order(3)
    void getHotelByIdIncludeDeleted(){
        Hotel result = hotelService.getHotelByIdIncludeDeleted(hotelId);

        assertNotNull(result);
        assertEquals(hotelId, result.getId());
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
        List<Room> result = hotelService.getRoomsByHotelId(hotelId);

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
        List<Room> result = hotelService.getRoomsByHotelIdDeletedTrue(hotelId);

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
        List<RoomType> result = hotelService.getRoomTypesByHotelId(hotelId);

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
        List<RoomType> result = hotelService.getRoomTypesByHotelIdDeletedTrue(hotelId);

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
        Hotel result = hotelService.updateHotel(hotelId, hotelToUpdate);

        assertNotNull(result);
        assertEquals(hotelId, result.getId());
        assertEquals(hotelToUpdate.getName(), result.getName());
        assertEquals(hotelToUpdate.getDescription(), result.getDescription());

        assertThrows(EntityNotFoundException.class, () -> {
            hotelService.updateHotel(-999L, hotelToUpdate);
        });


        hotelService.markAsDeletedHotel(hotelId);

        assertThrows(IllegalStateException.class, () -> {
            hotelService.updateHotel(hotelId, hotelToUpdate);
        });

        hotelService.markAsRestoredHotel(hotelId);
    }

    @Test
    @Order(10)
    void markAsDeletedHotel() {
        hotelService.markAsDeletedHotel(hotelId);

        assertThrows(IllegalStateException.class, () -> {
            hotelService.markAsDeletedHotel(hotelId);
        });
    }

    @Test
    @Order(11)
    void getHotelByIdDeletedTrue(){
        Hotel result = hotelService.getHotelByIdDeletedTrue(hotelId);

        assertNotNull(result);
        assertEquals(hotelId, result.getId());
        assertTrue(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            Hotel hotel = hotelService.getHotelByIdDeletedTrue(-999L);
        });
    }

    @Test
    @Order(12)
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
    @Order(13)
    void markAsRestoredHotel() {
        hotelService.markAsRestoredHotel(hotelId);

        assertThrows(IllegalStateException.class, () -> {
            hotelService.markAsRestoredHotel(hotelId);
        });
    }

    @Test
    @Order(14)
    void deleteHotel() {
        hotelService.deleteHotel(hotelId);

        assertThrows(EntityNotFoundException.class, () -> {
            hotelService.deleteHotel(hotelId);
        });
    }

}
