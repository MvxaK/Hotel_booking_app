package org.cook.booking_system.service.implementation.images;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.entity.images.HouseImageEntity;
import org.cook.booking_system.mapper.images.HouseImageMapper;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.repository.HouseRepository;
import org.cook.booking_system.repository.images.HouseImageRepository;
import org.cook.booking_system.service.service_interface.images.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseImageServiceImpl implements ImageService {

    private final HouseImageRepository houseImageRepository;
    private final HouseRepository houseRepository;
    private final HouseImageMapper houseImageMapper;

    @Transactional
    public Image addImage(Long houseId, String imageUrl) {
        HouseEntity house = houseRepository.findByIdAndDeletedFalse(houseId)
                .orElseThrow(() -> new EntityNotFoundException("House not found or marked as deleted with id -> " + houseId));

        HouseImageEntity image = new HouseImageEntity();
        image.setHouse(house);
        image.setUrl(imageUrl);

        return houseImageMapper.toModel(houseImageRepository.save(image));
    }

    @Transactional(readOnly = true)
    public List<Image> getImages(Long houseId) {
        HouseEntity house = houseRepository.findById(houseId)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + houseId));

        return houseImageRepository.findByHouseId(houseId).stream()
                .map(houseImageMapper::toModel)
                .toList();
    }

    @Transactional
    public void deleteImage(Long houseId, Long id) {
        HouseEntity house = houseRepository.findByIdAndDeletedFalse(houseId)
                .orElseThrow(() -> new EntityNotFoundException("House not found or marked as deleted with id -> " + houseId));

        if (!houseImageRepository.existsByIdAndHouseId(id, houseId)) {
            throw new EntityNotFoundException("Image not found with id -> " + id);
        }

        houseImageRepository.deleteById(id);
    }
}
