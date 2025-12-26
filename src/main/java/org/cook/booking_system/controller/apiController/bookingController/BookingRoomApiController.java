package org.cook.booking_system.controller.apiController.bookingController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.model.booking.BookingRoomRequest;
import org.cook.booking_system.service.service_interface.UserService;
import org.cook.booking_system.service.service_interface.booking.BookingRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bookings/rooms")
@RequiredArgsConstructor
public class BookingRoomApiController {

    private final BookingRoomService bookingRoomService;
    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BookingRoom> getBookingById(@PathVariable Long id) {
        BookingRoom booking = bookingRoomService.getBookingById(id);

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BookingRoom>> getAllBookingsByUserId(@PathVariable Long userId) {
        List<BookingRoom> bookings = bookingRoomService.getAllBookingByUserId(userId);

        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<BookingRoom> createBooking(@Valid @RequestBody BookingRoomRequest bookingRequest, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());

        BookingRoom booking = bookingRoomService.createBookingForUser(user.getId(), bookingRequest);
        URI location = URI.create("/api/bookings/rooms/" + booking.getId());

        return ResponseEntity.created(location)
                .body(booking);
    }

    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingRoomService.cancelBooking(id);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingRoomService.deleteBooking(id);

        return ResponseEntity.noContent()
                .build();
    }
}
