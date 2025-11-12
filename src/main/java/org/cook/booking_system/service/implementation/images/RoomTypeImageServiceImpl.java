package org.cook.booking_system.service.implementation.images;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.cook.booking_system.mapper.images.RoomTypeImageMapper;
import org.cook.booking_system.model.images.RoomTypeImage;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.repository.images.RoomTypeImageRepository;
import org.cook.booking_system.service.service_interface.images.RoomTypeImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeImageServiceImpl implements RoomTypeImageService {

    private final RoomTypeImageRepository roomTypeImageRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeImageMapper roomTypeImageMapper;

    @Transactional
    public RoomTypeImage addImage(Long roomTypeId, String imageUrl) {
        var roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found with id -> " + roomTypeId));

        RoomTypeImageEntity entity = new RoomTypeImageEntity();
        entity.setRoomType(roomType);
        entity.setUrl(imageUrl);

        return roomTypeImageMapper.toModel(roomTypeImageRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<RoomTypeImage> getImages(Long roomTypeId) {
        return roomTypeImageRepository.findByRoomTypeId(roomTypeId)
                .stream()
                .map(roomTypeImageMapper::toModel)
                .toList();
    }

    @Transactional
    public void deleteImage(Long id) {
        roomTypeImageRepository.deleteById(id);
    }
}
