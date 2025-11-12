package org.cook.booking_system.service.service_interface.images;

import org.cook.booking_system.model.images.HotelImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HotelImageService {

    HotelImage addImageToHotel(Long hotelId, String imageUrl);
    List<HotelImage> getImagesByHotelId(Long hotelId);
    void deleteImage(Long imageId);
}
