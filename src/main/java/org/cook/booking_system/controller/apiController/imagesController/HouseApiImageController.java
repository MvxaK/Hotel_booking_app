package org.cook.booking_system.controller.apiController.imagesController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.HouseImage;
import org.cook.booking_system.model.images.ImageRequest;
import org.cook.booking_system.service.implementation.images.HouseImageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses/{houseId}/images")
@RequiredArgsConstructor
public class HouseApiImageController {

    private final HouseImageServiceImpl houseImageService;

    @GetMapping
    public ResponseEntity<List<HouseImage>> getImages(@PathVariable Long houseId) {
        return ResponseEntity.ok(houseImageService.getImages(houseId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HouseImage> addImage(@PathVariable Long houseId, @RequestBody @Valid ImageRequest request) {

        return ResponseEntity.ok(houseImageService.addImage(houseId, request.getImageUrl()));
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long houseId, @PathVariable Long imageId) {
        houseImageService.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }
}