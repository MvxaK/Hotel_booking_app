package org.cook.booking_system.controller.apiController.bookingController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.booking.BookingHouse;
import org.cook.booking_system.model.booking.BookingHouseRequest;
import org.cook.booking_system.service.service_interface.UserService;
import org.cook.booking_system.service.service_interface.booking.BookingHouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bookings/houses")
@RequiredArgsConstructor
public class BookingHouseApiController {

    private final BookingHouseService bookingHouseService;
    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BookingHouse> getBookingById(@PathVariable Long id) {
        BookingHouse booking = bookingHouseService.getBookingById(id);

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BookingHouse>> getAllBookingsByUserId(@PathVariable Long userId) {
        List<BookingHouse> bookings = bookingHouseService.getAllBookingByUserId(userId);

        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<BookingHouse> createBooking(@Valid @RequestBody BookingHouseRequest bookingRequest, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());

        BookingHouse booking = bookingHouseService.createBookingForUser(user.getId(), bookingRequest);
        URI location = URI.create("/api/bookings/houses/" + booking.getId());

        return ResponseEntity.created(location)
                .body(booking);
    }

    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingHouseService.cancelBooking(id);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingHouseService.deleteBooking(id);

        return ResponseEntity.noContent()
                .build();
    }
}
