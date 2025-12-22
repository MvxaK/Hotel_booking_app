package org.cook.booking_system.images;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.cook.booking_system.service.implementation.images.RoomTypeImageServiceImpl;
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
public class RoomTypeImageServiceTest {

    @Autowired
    private RoomTypeImageServiceImpl roomTypeImageService;

    @Autowired
    private RoomTypeServiceImpl roomTypeService;

    @Autowired
    private HotelServiceImpl hotelService;

    private static Long testImageId;
    private static Long testRoomTypeId;
    private static Long testHotelId;
    private static final String url = "https://images.pexels.com/photos/164595/pexels-photo-164595.jpeg?cs=srgb&dl=pexels-pixabay-164595.jpg&fm=jpg";

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
        roomType.setPricePerNight(BigDecimal.valueOf(80000));
        roomType.setCapacity(2);
        roomType.setBedsCount(1);
        RoomType createdType = roomTypeService.createRoomTypeForHotel(roomType);
        testRoomTypeId = createdType.getId();

        assertNotNull(testHotelId);
        assertNotNull(testRoomTypeId);
    }

    @Test
    @Order(2)
    void addImage() {
        Image result = roomTypeImageService.addImage(testRoomTypeId, url);

        assertNotNull(result);
        assertEquals(url, result.getUrl());
        assertNotNull(result.getId());

        testImageId = result.getId();

        roomTypeService.markAsDeletedRoomType(testRoomTypeId);

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeImageService.addImage(testRoomTypeId, "https://dq5r178u4t83b.cloudfront.net/wp-content/uploads/sites/125/2020/06/15182916/Sofitel-Dubai-Wafi-Luxury-Room-Bedroom-Skyline-View-Image1_WEB.jpg");
        });

        roomTypeService.markAsRestoredRoomType(testRoomTypeId);
    }

    @Test
    @Order(3)
    void getImages() {
        List<Image> result = roomTypeImageService.getImages(testRoomTypeId);

        assertNotNull(result);
        for (Image image : result) {
            assertNotNull(image.getId());
            assertNotNull(image.getUrl());
            assertNotNull(image.getAccommodationId());
        }

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeImageService.getImages(-999L);
        });
    }

    @Test
    @Order(4)
    void deleteImage() {
        roomTypeService.markAsDeletedRoomType(testRoomTypeId);
        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeImageService.deleteImage(testRoomTypeId, testImageId);
        });
        roomTypeService.markAsRestoredRoomType(testRoomTypeId);

        roomTypeImageService.deleteImage(testRoomTypeId, testImageId);

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeImageService.deleteImage(testRoomTypeId, testImageId);
        });

        roomTypeService.deleteRoomType(testRoomTypeId);
        hotelService.deleteHotel(testHotelId);
    }
}