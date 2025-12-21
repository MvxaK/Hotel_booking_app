package org.cook.booking_system.service.implementation.images;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HotelEntity;
import org.cook.booking_system.entity.images.HotelImageEntity;
import org.cook.booking_system.mapper.images.HotelImageMapper;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.repository.HotelRepository;
import org.cook.booking_system.repository.images.HotelImageRepository;
import org.cook.booking_system.service.service_interface.images.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelImageServiceImpl implements ImageService {

    private final HotelImageRepository hotelImageRepository;
    private final HotelRepository hotelRepository;
    private final HotelImageMapper hotelImageMapper;

    @Transactional
    public Image addImage(Long hotelId, String imageUrl) {
        HotelEntity hotel = hotelRepository.findByIdAndDeletedFalse(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found or marked as deleted with id -> " + hotelId));

        HotelImageEntity image = new HotelImageEntity();
        image.setHotel(hotel);
        image.setUrl(imageUrl);

        return hotelImageMapper.toModel(hotelImageRepository.save(image));
    }

    @Transactional(readOnly = true)
    public List<Image> getImages(Long hotelId) {
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id -> " + hotelId));

        return hotelImageRepository.findByHotelId(hotelId).stream()
                .map(hotelImageMapper::toModel)
                .toList();
    }

    @Transactional
    public void deleteImage(Long hotelId, Long id) {
        HotelEntity hotel = hotelRepository.findByIdAndDeletedFalse(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found or marked as deleted with id -> " + hotelId));

        if (!hotelImageRepository.existsByIdAndHotelId(id, hotelId)) {
            throw new EntityNotFoundException("Image not found with id in hotel -> " + id);
        }

        hotelImageRepository.deleteById(id);
    }
}
