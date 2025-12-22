package org.cook.booking_system.service.implementation.images;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.cook.booking_system.mapper.images.RoomTypeImageMapper;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.repository.images.RoomTypeImageRepository;
import org.cook.booking_system.service.service_interface.images.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeImageServiceImpl implements ImageService {

    private final RoomTypeImageRepository roomTypeImageRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeImageMapper roomTypeImageMapper;

    @Transactional
    public Image addImage(Long roomTypeId, String imageUrl) {
        RoomTypeEntity roomType = roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found or marked as deleted with id -> " + roomTypeId));

        RoomTypeImageEntity image = new RoomTypeImageEntity();
        image.setRoomType(roomType);
        image.setUrl(imageUrl);

        return roomTypeImageMapper.toModel(roomTypeImageRepository.save(image));
    }

    @Transactional(readOnly = true)
    public List<Image> getImages(Long roomTypeId) {
        RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found with id -> " + roomTypeId));

        return roomTypeImageRepository.findByRoomTypeId(roomTypeId).stream()
                .map(roomTypeImageMapper::toModel)
                .toList();
    }

    @Transactional
    public void deleteImage(Long roomTypeId, Long id) {
        RoomTypeEntity roomType = roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found or marked as deleted with id -> " + roomTypeId));

        if (!roomTypeImageRepository.existsByIdAndRoomTypeId(id, roomTypeId)) {
            throw new EntityNotFoundException("Image not found with id in roomType -> " + id);
        }

        roomTypeImageRepository.deleteById(id);
    }
}
