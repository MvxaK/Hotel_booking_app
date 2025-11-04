package org.cook.booking_system.controller.imagesController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.RoomTypeImage;
import org.cook.booking_system.service.images.RoomTypeImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room-types/{roomTypeId}/images")
@RequiredArgsConstructor
public class RoomTypeImageController {

    private final RoomTypeImageService service;

    @PostMapping
    public ResponseEntity<RoomTypeImage> addImage(@PathVariable Long roomTypeId, @RequestBody Map<String, String> request) {
        String imageUrl = request.get("imageUrl");
        if (imageUrl == null || imageUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(service.addImage(roomTypeId, imageUrl));
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeImage>> getImages(@PathVariable Long roomTypeId) {
        return ResponseEntity.ok(service.getImages(roomTypeId));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long roomTypeId, @PathVariable Long imageId) {
        service.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }
}

