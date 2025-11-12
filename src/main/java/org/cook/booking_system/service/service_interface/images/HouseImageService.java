package org.cook.booking_system.service.service_interface.images;

import org.cook.booking_system.model.images.HouseImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseImageService {
    HouseImage addImage(Long houseId, String imageUrl);
    List<HouseImage> getImages(Long houseId);
    void deleteImage(Long imageId);
}

