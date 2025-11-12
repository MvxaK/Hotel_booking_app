package org.cook.booking_system.controller.imagesController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.HouseImage;
import org.cook.booking_system.service.implementation.images.HouseImageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses/{houseId}/images")
@RequiredArgsConstructor
public class HouseImageController {

    private final HouseImageServiceImpl houseImageService;

    @PostMapping
    public ResponseEntity<HouseImage> addImage(@PathVariable Long houseId, @RequestParam String imageUrl) {
        return ResponseEntity.ok(houseImageService.addImage(houseId, imageUrl));
    }

    @GetMapping
    public ResponseEntity<List<HouseImage>> getImages(@PathVariable Long houseId) {
        return ResponseEntity.ok(houseImageService.getImages(houseId));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long houseId, @PathVariable Long imageId) {
        houseImageService.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }
}