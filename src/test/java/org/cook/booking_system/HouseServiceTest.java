package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
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
public class HouseServiceTest {

    @Autowired
    private HouseServiceImpl houseService;

    private static Long houseId;

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

        houseId = result.getId();
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
        House result = houseService.getHouseById(houseId);

        assertNotNull(result);
        assertEquals(houseId, result.getId());
        assertFalse(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            houseService.getHouseById(-999L);
        });
    }

    @Test
    @Order(4)
    void updateHouse() {
        House result = houseService.updateHouse(houseId, houseToUpdate);

        assertNotNull(result);

        assertEquals(houseId, result.getId());
        assertEquals(houseToUpdate.getCapacity(), result.getCapacity());
        assertEquals(houseToUpdate.getPricePerNight(), result.getPricePerNight());
        assertEquals(houseToUpdate.getBedsCount(), result.getBedsCount());
        assertEquals(houseToUpdate.getParkingSlots(), result.getParkingSlots());


        assertThrows(EntityNotFoundException.class, () -> {
            houseService.updateHouse(-999L, houseToUpdate);
        });


        houseService.markAsDeletedHouse(houseId);

        assertThrows(IllegalStateException.class, () -> {
            houseService.updateHouse(houseId, houseToUpdate);
        });

        houseService.markAsRestoredHouse(houseId);
    }

    @Test
    @Order(5)
    void markAsDeletedHouse() {
        houseService.markAsDeletedHouse(houseId);

        assertThrows(IllegalStateException.class, () -> {
            houseService.markAsDeletedHouse(houseId);
        });
    }

    @Test
    @Order(6)
    void getHouseByIdDeletedTrue() {
        House result = houseService.getHouseByIdDeletedTrue(houseId);

        assertNotNull(result);
        assertEquals(houseId, result.getId());
        assertTrue(result.isDeleted());

        assertThrows(EntityNotFoundException.class, () -> {
            houseService.getHouseById(-999L);
        });
    }

    @Test
    @Order(7)
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
    @Order(8)
    void markAsRestoredHouse() {
        houseService.markAsRestoredHouse(houseId);

        assertThrows(IllegalStateException.class, () -> {
            houseService.markAsRestoredHouse(houseId);
        });
    }

    @Test
    @Order(9)
    void deleteHouse() {
        houseService.deleteHouse(houseId);

        assertThrows(EntityNotFoundException.class, () -> {
            houseService.deleteHouse(houseId);
        });
    }


}
