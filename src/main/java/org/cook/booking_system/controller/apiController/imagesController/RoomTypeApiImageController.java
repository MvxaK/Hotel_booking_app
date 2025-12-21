package org.cook.booking_system.controller.apiController.imagesController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.model.images.ImageRequest;
import org.cook.booking_system.service.implementation.images.RoomTypeImageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types/{roomTypeId}/images")
@RequiredArgsConstructor
public class RoomTypeApiImageController {

    private final RoomTypeImageServiceImpl roomTypeImageService;

    @GetMapping
    public ResponseEntity<List<Image>> getImages(@PathVariable Long roomTypeId) {
        List<Image> images = roomTypeImageService.getImages(roomTypeId);

        return ResponseEntity.ok(images);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Image> addImage(@PathVariable Long roomTypeId, @RequestBody @Valid ImageRequest request) {
        Image image = roomTypeImageService.addImage(roomTypeId, request.getImageUrl());

        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long roomTypeId, @PathVariable Long imageId) {
        roomTypeImageService.deleteImage(roomTypeId, imageId);

        return ResponseEntity.noContent()
                .build();
    }
}

