package org.cook.booking_system.images;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.cook.booking_system.mapper.images.RoomTypeImageMapper;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.repository.images.RoomTypeImageRepository;
import org.cook.booking_system.service.implementation.images.RoomTypeImageServiceImpl;
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
public class RoomTypeImageServiceTest {

    @Mock
    private RoomTypeImageRepository roomTypeImageRepository;

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @Mock
    private RoomTypeImageMapper roomTypeImageMapper;

    @InjectMocks
    private RoomTypeImageServiceImpl roomTypeImageService;

    private RoomTypeEntity roomTypeEntity;
    private RoomTypeImageEntity roomTypeImageEntity;
    private Image image;

    private final Long roomTypeId = 1L;
    private final Long imageId = 42L;
    private final String url = "https://cdn-icons-png.flaticon.com/512/8068/8068148.png";

    @BeforeEach
    void testData(){
        roomTypeEntity = new RoomTypeEntity();
        roomTypeEntity.setId(roomTypeId);
        roomTypeEntity.setDeleted(false);

        roomTypeImageEntity = new RoomTypeImageEntity();
        roomTypeImageEntity.setId(imageId);
        roomTypeImageEntity.setUrl(url);
        roomTypeImageEntity.setRoomType(roomTypeEntity);

        image = new Image();
        image.setId(imageId);
        image.setUrl(url);
    }

    @Test
    void addImage(){
        when(roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)).thenReturn(Optional.of(roomTypeEntity));
        when(roomTypeImageRepository.save(any(RoomTypeImageEntity.class))).thenReturn(roomTypeImageEntity);
        when(roomTypeImageMapper.toModel(any(RoomTypeImageEntity.class))).thenReturn(image);

        Image result = roomTypeImageService.addImage(roomTypeId, url);

        assertNotNull(result);
        verify(roomTypeRepository).findByIdAndDeletedFalse(roomTypeId);
        verify(roomTypeImageRepository).save(any());
    }

    @Test
    void addImage_RoomTypeNotFoundOrDeleted(){
        when(roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            Image result = roomTypeImageService.addImage(roomTypeId, url);
        });
    }

    @Test
    void getImages(){
        when(roomTypeRepository.findById(roomTypeId)).thenReturn(Optional.of(roomTypeEntity));
        when(roomTypeImageRepository.findByRoomTypeId(roomTypeId)).thenReturn(List.of(roomTypeImageEntity, roomTypeImageEntity));
        when(roomTypeImageMapper.toModel(roomTypeImageEntity)).thenReturn(image);

        List<Image> result = roomTypeImageService.getImages(roomTypeId);

        assertEquals(2, result.size());
        verify(roomTypeImageRepository).findByRoomTypeId(roomTypeId);
    }

    @Test
    void deleteImage() {
        when(roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)).thenReturn(Optional.of(roomTypeEntity));
        when(roomTypeImageRepository.existsByIdAndRoomTypeId(imageId, roomTypeId)).thenReturn(true);

        roomTypeImageService.deleteImage(roomTypeId, imageId);

        verify(roomTypeImageRepository).deleteById(imageId);
    }

    @Test
    void deleteImage_RoomTypeNotFoundOrDeleted() {
        when(roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeImageService.deleteImage(roomTypeId, imageId);
        });
    }

    @Test
    void deleteImage_ImageNotBelongsToRoomType() {
        when(roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)).thenReturn(Optional.of(roomTypeEntity));
        when(roomTypeImageRepository.existsByIdAndRoomTypeId(imageId, roomTypeId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            roomTypeImageService.deleteImage(roomTypeId, imageId);
        });
    }

}
