package org.cook.booking_system.service.implementation.images;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.entity.images.HouseImageEntity;
import org.cook.booking_system.mapper.images.HouseImageMapper;
import org.cook.booking_system.model.images.HouseImage;
import org.cook.booking_system.repository.HouseRepository;
import org.cook.booking_system.repository.images.HouseImageRepository;
import org.cook.booking_system.service.service_interface.images.HouseImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseImageServiceImpl implements HouseImageService{
    private final HouseImageRepository houseImageRepository;
    private final HouseRepository houseRepository;
    private final HouseImageMapper houseImageMapper;

    @Transactional
    public HouseImage addImage(Long houseId, String imageUrl) {
        HouseEntity house = houseRepository.findById(houseId)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + houseId));

        HouseImageEntity entity = new HouseImageEntity();
        entity.setHouse(house);
        entity.setUrl(imageUrl);

        return houseImageMapper.toModel(houseImageRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<HouseImage> getImages(Long houseId) {
        return houseImageRepository.findByHouseId(houseId)
                .stream()
                .map(houseImageMapper::toModel)
                .toList();
    }

    @Transactional
    public void deleteImage(Long imageId) {
        if (!houseImageRepository.existsById(imageId)) {
            throw new EntityNotFoundException("Image not found with id -> " + imageId);
        }
        houseImageRepository.deleteById(imageId);
    }
}
