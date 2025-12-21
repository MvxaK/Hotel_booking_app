package org.cook.booking_system.controller.apiController.imagesController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.Image;
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
    public ResponseEntity<List<Image>> getImages(@PathVariable Long houseId) {
        List<Image> images = houseImageService.getImages(houseId);

        return ResponseEntity.ok(images);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<Image> addImage(@PathVariable Long houseId, @RequestBody @Valid ImageRequest request) {
        Image image = houseImageService.addImage(houseId, request.getImageUrl());

        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long houseId, @PathVariable Long imageId) {
        houseImageService.deleteImage(houseId, imageId);

        return ResponseEntity.noContent()
                .build();
    }
}