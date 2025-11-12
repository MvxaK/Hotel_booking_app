package org.cook.booking_system.service.implementation.images;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.images.HotelImageEntity;
import org.cook.booking_system.mapper.images.HotelImageMapper;
import org.cook.booking_system.model.images.HotelImage;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.images.HotelImageRepository;
import org.cook.booking_system.service.service_interface.images.HotelImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelImageServiceImpl implements HotelImageService {
    private final HotelImageRepository hotelImageRepository;
    private final HotelRepository hotelRepository;
    private final HotelImageMapper hotelImageMapper;

    @Transactional
    public HotelImage addImageToHotel(Long hotelId, String imageUrl) {
        var hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + hotelId));

        HotelImageEntity imageEntity = new HotelImageEntity();
        imageEntity.setHotel(hotel);
        imageEntity.setUrl(imageUrl);

        return hotelImageMapper.toModel(hotelImageRepository.save(imageEntity));
    }

    @Transactional(readOnly = true)
    public List<HotelImage> getImagesByHotelId(Long hotelId) {
        return hotelImageRepository.findByHotelId(hotelId)
                .stream()
                .map(hotelImageMapper::toModel)
                .toList();
    }

    @Transactional
    public void deleteImage(Long imageId) {
        if (!hotelImageRepository.existsById(imageId)) {
            throw new EntityNotFoundException("Image not found with id -> " + imageId);
        }

        hotelImageRepository.deleteById(imageId);
    }
}
