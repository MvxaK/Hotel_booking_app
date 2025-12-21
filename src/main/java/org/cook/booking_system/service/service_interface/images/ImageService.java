package org.cook.booking_system.service.service_interface.images;

import org.cook.booking_system.model.images.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {

    Image addImage(Long roomTypeId, String imageUrl);
    List<Image> getImages(Long roomTypeId);
    void deleteImage(Long accommodationId, Long id);

}
