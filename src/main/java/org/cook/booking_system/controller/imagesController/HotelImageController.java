package org.cook.booking_system.controller.imagesController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.HotelImage;
import org.cook.booking_system.service.images.HotelImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels/{hotelId}/images")
@RequiredArgsConstructor
public class HotelImageController {

    private final HotelImageService hotelImageService;

    @GetMapping
    public ResponseEntity<List<HotelImage>> getHotelImages(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelImageService.getImagesByHotelId(hotelId));
    }

    @PostMapping
    public ResponseEntity<HotelImage> addImage(@PathVariable Long hotelId, @RequestBody Map<String, String> request) {
        String imageUrl = request.get("imageUrl");
        if (imageUrl == null || imageUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(hotelImageService.addImageToHotel(hotelId, imageUrl));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long hotelId, @PathVariable Long imageId) {
        hotelImageService.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }
}

