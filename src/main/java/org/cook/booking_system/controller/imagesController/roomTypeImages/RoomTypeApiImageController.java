package org.cook.booking_system.controller.imagesController.roomTypeImages;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.ImageRequest;
import org.cook.booking_system.model.images.RoomTypeImage;
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
    public ResponseEntity<List<RoomTypeImage>> getImages(@PathVariable Long roomTypeId) {
        return ResponseEntity.ok(roomTypeImageService.getImages(roomTypeId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomTypeImage> addImage(@PathVariable Long roomTypeId, @RequestBody @Valid ImageRequest request) {

        return ResponseEntity.ok(roomTypeImageService.addImage(roomTypeId, request.getImageUrl()));
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long roomTypeId, @PathVariable Long imageId) {
        roomTypeImageService.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }
}

