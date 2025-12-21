package org.cook.booking_system.images;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.entity.images.HouseImageEntity;
import org.cook.booking_system.mapper.images.HouseImageMapper;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.repository.HouseRepository;
import org.cook.booking_system.repository.images.HouseImageRepository;
import org.cook.booking_system.service.implementation.images.HouseImageServiceImpl;
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
public class HouseImageServiceTest {

    @Mock
    private HouseImageRepository houseImageRepository;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseImageMapper houseImageMapper;

    @InjectMocks
    private HouseImageServiceImpl houseImageService;

    private HouseEntity houseEntity;
    private HouseImageEntity houseImageEntity;
    private Image image;

    private final Long houseId = 1L;
    private final Long imageId = 42L;
    private final String url = "https://cdn-icons-png.flaticon.com/512/8068/8068148.png";

    @BeforeEach
    void testData(){
        houseEntity = new HouseEntity();
        houseEntity.setId(houseId);
        houseEntity.setDeleted(false);

        houseImageEntity = new HouseImageEntity();
        houseImageEntity.setId(imageId);
        houseImageEntity.setUrl(url);
        houseImageEntity.setHouse(houseEntity);

        image = new Image();
        image.setId(imageId);
        image.setUrl(url);
    }

    @Test
    void addImage(){
        when(houseRepository.findByIdAndDeletedFalse(houseId)).thenReturn(Optional.of(houseEntity));
        when(houseImageRepository.save(any(HouseImageEntity.class))).thenReturn(houseImageEntity);
        when(houseImageMapper.toModel(any(HouseImageEntity.class))).thenReturn(image);

        Image result = houseImageService.addImage(houseId, url);

        assertNotNull(result);
        verify(houseRepository).findByIdAndDeletedFalse(houseId);
        verify(houseImageRepository).save(any());
    }

    @Test
    void addImage_HouseNotFoundOrDeleted(){
        when(houseRepository.findByIdAndDeletedFalse(houseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            Image result = houseImageService.addImage(houseId, url);
        });
    }

    @Test
    void getImages(){
        when(houseRepository.findById(houseId)).thenReturn(Optional.of(houseEntity));
        when(houseImageRepository.findByHouseId(houseId)).thenReturn(List.of(houseImageEntity, houseImageEntity));
        when(houseImageMapper.toModel(houseImageEntity)).thenReturn(image);

        List<Image> result = houseImageService.getImages(houseId);

        assertEquals(2, result.size());
        verify(houseImageRepository).findByHouseId(houseId);
    }

    @Test
    void deleteImage() {
        when(houseRepository.findByIdAndDeletedFalse(houseId)).thenReturn(Optional.of(houseEntity));
        when(houseImageRepository.existsByIdAndHouseId(imageId, houseId)).thenReturn(true);

        houseImageService.deleteImage(houseId, imageId);

        verify(houseImageRepository).deleteById(imageId);
    }

    @Test
    void deleteImage_HouseNotFoundOrDeleted() {
        when(houseRepository.findByIdAndDeletedFalse(houseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            houseImageService.deleteImage(houseId, imageId);
        });
    }

    @Test
    void deleteImage_ImageNotBelongsToHouse() {
        when(houseRepository.findByIdAndDeletedFalse(houseId)).thenReturn(Optional.of(houseEntity));
        when(houseImageRepository.existsByIdAndHouseId(imageId, houseId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            houseImageService.deleteImage(houseId, imageId);
        });
    }

}
