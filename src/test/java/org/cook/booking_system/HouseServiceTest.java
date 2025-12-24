package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.House;
import org.cook.booking_system.model.booking.BookingHouseRequest;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.repository.booking.BookingHouseRepository;
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
public class HouseServiceTest {

    @Autowired
    private HouseServiceImpl houseService;

    @Autowired
    private BookingHouseServiceImpl bookingHouseService;

    @Autowired
    private BookingHouseRepository bookingHouseRepository;

    @Autowired
    private UserRepository userRepository;

    private static Long testHouseId;
    private static Long testUserId;

    private House house = new House();
    private House houseToUpdate = new House();

    @BeforeEach
    void houseData(){
        house.setName("Amazing House");
        house.setLocation("St. Something 42");
        house.setCapacity(10);
        house.setPricePerNight(BigDecimal.valueOf(100000));
        house.setAvailable(true);
        house.setRoomsNumber(4);
        house.setBedsCount(6);
        house.setParkingSlots(4);
        house.setDescription("Amazing description");

        houseToUpdate.setName("Amazing House");
        houseToUpdate.setLocation("St. Something 42");
        houseToUpdate.setCapacity(6);
        houseToUpdate.setPricePerNight(BigDecimal.valueOf(150000));
        houseToUpdate.setAvailable(false);
        houseToUpdate.setRoomsNumber(4);
        houseToUpdate.setBedsCount(3);
        houseToUpdate.setParkingSlots(2);
        houseToUpdate.setDescription("Amazing description");
    }

    @Test
    @Order(1)
    void createHouse() {
        House result = houseService.createHouse(house);

        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getPricePerNight());
        assertTrue(result.isAvailable());
        assertFalse(result.isDeleted());

        testHouseId = result.getId();

        UserEntity user = new UserEntity();
        user.setUserName("Immortal_NoName");
        user.setEmail("immortal_noname@gmail.com");
        user.setPassword("noname");

        testUserId = userRepository.save(user).getId();
    }

    @Test
    @Order(2)
    void getAllHouses() {
        List<House> result = houseService.getAllHouses();

        assertNotNull(result);
        for (House house: result){
            assertNotNull(house.getId());
            assertNotNull(house.getPricePerNight());
            assertNotNull(house.getDescription());
            assertFalse(house.isDeleted());
        }
    }

    @Test
    @Order(3)
    void getHouseById() {
        House result = houseService.getHouseById(testHouseId);

        assertNotNull(result);
        assertEquals(testHouseId, result.getId());
        assertFalse(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            houseService.getHouseById(-999L);
        });
    }

    @Test
    @Order(4)
    void updateHouse() {
        House result = houseService.updateHouse(testHouseId, houseToUpdate);

        assertNotNull(result);

        assertEquals(testHouseId, result.getId());
        assertEquals(houseToUpdate.getCapacity(), result.getCapacity());
        assertEquals(houseToUpdate.getPricePerNight(), result.getPricePerNight());
        assertEquals(houseToUpdate.getBedsCount(), result.getBedsCount());
        assertEquals(houseToUpdate.getParkingSlots(), result.getParkingSlots());


        assertThrows(EntityNotFoundException.class, () -> {
            houseService.updateHouse(-999L, houseToUpdate);
        });


        houseService.markAsDeletedHouse(testHouseId);

        assertThrows(IllegalStateException.class, () -> {
            houseService.updateHouse(testHouseId, houseToUpdate);
        });

        houseService.markAsRestoredHouse(testHouseId);
    }

    @Test
    @Order(5)
    void deleteHouse_markAsDeletedHouse_hasActiveBookings() {
        BookingHouseRequest request = new BookingHouseRequest();
        request.setHouseId(testHouseId);
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(5));

        bookingHouseService.createBookingForUser(testUserId, request);

        assertThrows(IllegalStateException.class, () -> {
            houseService.markAsDeletedHouse(testHouseId);
        });

        assertThrows(IllegalStateException.class, () -> {
            houseService.deleteHouse(testHouseId);
        });

        bookingHouseRepository.deleteByHouseId(testHouseId);
        userRepository.deleteById(testUserId);
    }

    @Test
    @Order(6)
    void markAsDeletedHouse() {
        houseService.markAsDeletedHouse(testHouseId);

        assertThrows(IllegalStateException.class, () -> {
            houseService.markAsDeletedHouse(testHouseId);
        });
    }

    @Test
    @Order(7)
    void getHouseByIdDeletedTrue() {
        House result = houseService.getHouseByIdDeletedTrue(testHouseId);

        assertNotNull(result);
        assertEquals(testHouseId, result.getId());
        assertTrue(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            houseService.getHouseById(-999L);
        });
    }

    @Test
    @Order(8)
    void getAllHousesDeletedTrue() {
        List<House> result = houseService.getAllHousesDeletedTrue();

        assertNotNull(result);
        for (House house: result){
            assertNotNull(house.getId());
            assertNotNull(house.getPricePerNight());
            assertNotNull(house.getDescription());
            assertTrue(house.isDeleted());
        }
    }

    @Test
    @Order(9)
    void markAsRestoredHouse() {
        houseService.markAsRestoredHouse(testHouseId);

        assertThrows(IllegalStateException.class, () -> {
            houseService.markAsRestoredHouse(testHouseId);
        });
    }

    @Test
    @Order(10)
    void deleteHouse() {
        houseService.deleteHouse(testHouseId);

        assertThrows(EntityNotFoundException.class, () -> {
            houseService.deleteHouse(testHouseId);
        });
    }


}
