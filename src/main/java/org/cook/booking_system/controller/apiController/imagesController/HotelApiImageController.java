package org.cook.booking_system.controller.apiController.imagesController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.Image;
import org.cook.booking_system.model.images.ImageRequest;
import org.cook.booking_system.service.implementation.images.HotelImageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels/{hotelId}/images")
@RequiredArgsConstructor
public class HotelApiImageController {

    private final HotelImageServiceImpl hotelImageService;

    @GetMapping
    public ResponseEntity<List<Image>> getImages(@PathVariable Long hotelId) {
        List<Image> images = hotelImageService.getImages(hotelId);

        return ResponseEntity.ok(images);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Image> addImage(@PathVariable Long hotelId, @RequestBody @Valid ImageRequest request) {
        Image image = hotelImageService.addImage(hotelId, request.getImageUrl());

        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long hotelId, @PathVariable Long imageId) {
        hotelImageService.deleteImage(hotelId, imageId);

        return ResponseEntity.noContent()
                .build();
    }
}

