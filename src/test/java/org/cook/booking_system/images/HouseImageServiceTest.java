package org.cook.booking_system.images;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.model.House;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
import org.cook.booking_system.service.implementation.images.HouseImageServiceImpl;
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
public class HouseImageServiceTest {

    @Autowired
    private HouseImageServiceImpl houseImageService;

    @Autowired
    private HouseServiceImpl houseService;

    private static Long testImageId;
    private static Long testHouseId;
    private static final String url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTapw_XWWJYkuY2JzBMXZsmTWtaGHL3p60_kw&s";

    @Test
    @Order(1)
    void setupDependency() {
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

        House createdHouse = houseService.createHouse(house);
        testHouseId = createdHouse.getId();

        assertNotNull(testHouseId);
    }

    @Test
    @Order(2)
    void addImage() {
        Image result = houseImageService.addImage(testHouseId, url);

        assertNotNull(result);
        assertEquals(url, result.getUrl());
        assertNotNull(result.getId());

        testImageId = result.getId();

        houseService.markAsDeletedHouse(testHouseId);

        assertThrows(EntityNotFoundException.class, () -> {
            houseImageService.addImage(testHouseId, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS7Tp989cbb6ZhyWuRBgQqOApupbpLgf7nuLw&s");
        });

        houseService.markAsRestoredHouse(testHouseId);
    }

    @Test
    @Order(3)
    void getImages() {
        List<Image> result = houseImageService.getImages(testHouseId);

        assertNotNull(result);
        for (Image image : result) {
            assertNotNull(image.getId());
            assertNotNull(image.getUrl());
            assertNotNull(image.getAccommodationId());
        }

        assertThrows(EntityNotFoundException.class, () -> {
            houseImageService.getImages(-999L);
        });
    }

    @Test
    @Order(4)
    void deleteImage() {
        houseService.markAsDeletedHouse(testHouseId);
        assertThrows(EntityNotFoundException.class, () -> {
            houseImageService.deleteImage(testHouseId, testImageId);
        });
        houseService.markAsRestoredHouse(testHouseId);

        houseImageService.deleteImage(testHouseId, testImageId);

        assertThrows(EntityNotFoundException.class, () -> {
            houseImageService.deleteImage(testHouseId, testImageId);
        });

        assertThrows(EntityNotFoundException.class, () -> {
            houseImageService.deleteImage(-999L, testImageId);
        });

        houseService.deleteHouse(testHouseId);
    }
}