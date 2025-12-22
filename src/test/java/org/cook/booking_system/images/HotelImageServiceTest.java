package org.cook.booking_system.images;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.images.HotelImageServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelImageServiceTest {

    @Autowired
    private HotelImageServiceImpl hotelImageService;

    @Autowired
    private HotelServiceImpl hotelService;

    private static Long testImageId;
    private static Long testHotelId;
    private static final String url = "https://m.ahstatic.com/is/image/accorhotels/HCM_P_8147067:4by3?fmt=jpg&op_usm=1.75,0.3,2,0&resMode=sharp2&iccEmbed=true&icc=sRGB&dpr=on,1.5&wid=335&hei=251&qlt=80";

    @Test
    @Order(1)
    void setupDependency() {
        Hotel hotel = new Hotel();
        hotel.setName("Amazing Hotel");
        hotel.setAddress("St. Somewhere 42");
        hotel.setDescription("Amazing Hotel description");

        Hotel createdHotel = hotelService.createHotel(hotel);
        testHotelId = createdHotel.getId();

        assertNotNull(testHotelId);
    }

    @Test
    @Order(2)
    void addImage() {
        Image result = hotelImageService.addImage(testHotelId, url);

        assertNotNull(result);
        assertEquals(url, result.getUrl());
        assertNotNull(result.getId());

        testImageId = result.getId();

        hotelService.markAsDeletedHotel(testHotelId);

        assertThrows(EntityNotFoundException.class, () -> {
            hotelImageService.addImage(testHotelId, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShoZE9NzCklTm5urZecdcb6aYdQU69fUnJMw&s");
        });

        hotelService.markAsRestoredHotel(testHotelId);
    }

    @Test
    @Order(3)
    void getImages() {
        List<Image> result = hotelImageService.getImages(testHotelId);

        assertNotNull(result);
        for (Image image: result){
            assertNotNull(image.getId());
            assertNotNull(image.getUrl());
            assertNotNull(image.getAccommodationId());
        }

        assertThrows(EntityNotFoundException.class, () -> {
            hotelImageService.getImages(-999L);
        });
    }

    @Test
    @Order(4)
    void deleteImage() {
        hotelService.markAsDeletedHotel(testHotelId);
        assertThrows(EntityNotFoundException.class, () -> {
            hotelImageService.deleteImage(testHotelId, testImageId);
        });
        hotelService.markAsRestoredHotel(testHotelId);

        hotelImageService.deleteImage(testHotelId, testImageId);

        assertThrows(EntityNotFoundException.class, () -> {
            hotelImageService.deleteImage(testHotelId, testImageId);
        });

        assertThrows(EntityNotFoundException.class, () -> {
            hotelImageService.deleteImage(-999L, testImageId);
        });

        hotelService.deleteHotel(testHotelId);
    }
}