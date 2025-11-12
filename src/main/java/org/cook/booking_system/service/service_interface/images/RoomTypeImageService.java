package org.cook.booking_system.service.service_interface.images;

import org.cook.booking_system.model.images.RoomTypeImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomTypeImageService {

    RoomTypeImage addImage(Long roomTypeId, String imageUrl);
    List<RoomTypeImage> getImages(Long roomTypeId);
    void deleteImage(Long id);
}
