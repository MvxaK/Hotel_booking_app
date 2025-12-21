package org.cook.booking_system.images;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.images.HotelImageEntity;
import org.cook.booking_system.mapper.images.HotelImageMapper;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.images.HotelImageRepository;
import org.cook.booking_system.service.implementation.images.HotelImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelImageServiceTest {

    @Mock
    private HotelImageRepository hotelImageRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelImageMapper hotelImageMapper;

    @InjectMocks
    private HotelImageServiceImpl hotelImageService;

    private HotelEntity hotelEntity;
    private HotelImageEntity hotelImageEntity;
    private Image image;

    private final Long hotelId = 1L;
    private final Long imageId = 42L;
    private final String url = "https://cdn-icons-png.flaticon.com/512/8068/8068148.png";

    @BeforeEach
    void testData(){
        hotelEntity = new HotelEntity();
        hotelEntity.setId(hotelId);
        hotelEntity.setName("Great Cat Hotel");

        hotelImageEntity = new HotelImageEntity();
        hotelImageEntity.setId(imageId);
        hotelImageEntity.setUrl(url);
        hotelImageEntity.setHotel(hotelEntity);

        image = new Image();
        image.setId(imageId);
        image.setUrl(url);
        image.setAccommodationId(hotelId);
    }

    @Test
    void addImage(){
        when(hotelRepository.findByIdAndDeletedFalse(hotelId)).thenReturn(Optional.of(hotelEntity));
        when(hotelImageRepository.save(any(HotelImageEntity.class))).thenReturn(hotelImageEntity);
        when(hotelImageMapper.toModel(any(HotelImageEntity.class))).thenReturn(image);

        Image result = hotelImageService.addImage(hotelId, url);

        assertNotNull(result);
        verify(hotelRepository).findByIdAndDeletedFalse(hotelId);
        verify(hotelImageRepository).save(any());
    }

    @Test
    void addImage_HotelNotFoundOrDeleted(){
        when(hotelRepository.findByIdAndDeletedFalse(hotelId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            Image result = hotelImageService.addImage(hotelId, url);
        });
    }

    @Test
    void getImages(){
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));
        when(hotelImageRepository.findByHotelId(hotelId)).thenReturn(List.of(hotelImageEntity, hotelImageEntity));
        when(hotelImageMapper.toModel(hotelImageEntity)).thenReturn(image);

        List<Image> result = hotelImageService.getImages(hotelId);

        assertEquals(2, result.size());
        verify(hotelImageRepository).findByHotelId(hotelId);
    }

    @Test
    void deleteImage() {
        when(hotelRepository.findByIdAndDeletedFalse(hotelId)).thenReturn(Optional.of(hotelEntity));
        when(hotelImageRepository.existsByIdAndHotelId(imageId, hotelId)).thenReturn(true);

        hotelImageService.deleteImage(hotelId, imageId);

        verify(hotelImageRepository).deleteById(imageId);
    }

    @Test
    void deleteImage_HotelNotFoundOrDeleted() {
        when(hotelRepository.findByIdAndDeletedFalse(hotelId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            hotelImageService.deleteImage(hotelId, imageId);
        });
    }

    @Test
    void deleteImage_ImageNotBelongsToHotel() {
        when(hotelRepository.findByIdAndDeletedFalse(hotelId)).thenReturn(Optional.of(hotelEntity));
        when(hotelImageRepository.existsByIdAndHotelId(imageId, hotelId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            hotelImageService.deleteImage(hotelId, imageId);
        });
    }

}
