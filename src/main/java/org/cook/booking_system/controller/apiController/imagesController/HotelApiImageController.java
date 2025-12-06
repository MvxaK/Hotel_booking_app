package org.cook.booking_system.controller.apiController.imagesController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.images.HotelImage;
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
    public ResponseEntity<List<HotelImage>> getImages(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelImageService.getImagesByHotelId(hotelId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HotelImage> addImage(@PathVariable Long hotelId, @RequestBody @Valid ImageRequest request) {

        return ResponseEntity.ok(hotelImageService.addImageToHotel(hotelId, request.getImageUrl()));
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long hotelId, @PathVariable Long imageId) {
        hotelImageService.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }
}

