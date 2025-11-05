package org.cook.booking_system.service.implementation.images;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.images.RoomTypeImageEntity;
import org.cook.booking_system.mapper.images.RoomTypeImageMapper;
import org.cook.booking_system.model.images.RoomTypeImage;
import org.cook.booking_system.repository.RoomTypeRepository;
import org.cook.booking_system.repository.images.RoomTypeImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeImageImpl {
    private final RoomTypeImageRepository repository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeImageMapper mapper;

    @Transactional
    public RoomTypeImage addImage(Long roomTypeId, String imageUrl) {
        var roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found with id -> " + roomTypeId));

        RoomTypeImageEntity entity = new RoomTypeImageEntity();
        entity.setRoomType(roomType);
        entity.setUrl(imageUrl);

        return mapper.toModel(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<RoomTypeImage> getImages(Long roomTypeId) {
        return repository.findByRoomTypeId(roomTypeId)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Transactional
    public void deleteImage(Long id) {
        repository.deleteById(id);
    }
}
