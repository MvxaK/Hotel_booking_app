package org.cook.booking_system.controller.bookingController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.model.booking.BookingRoomRequest;
import org.cook.booking_system.service.booking.BookingRoomService;
import org.cook.booking_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/booking/room")
@RequiredArgsConstructor
public class BookingRoomApiController {

    private final BookingRoomService bookingRoomService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingRoom> getBookingById(@PathVariable Long id) {
        BookingRoom booking = bookingRoomService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingRoom>> getAllBookingsByUserId(@PathVariable Long userId) {
        List<BookingRoom> bookings = bookingRoomService.getAllBookingByUserId(userId);

        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<BookingRoom> createBooking(@RequestBody BookingRoomRequest bookingRequest, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserName(userDetails.getUsername());

        BookingRoom booking = bookingRoomService.createBookingForUser(user.getId(), bookingRequest);
        URI location = URI.create("/api/booking/room/" + booking.getId());

        return ResponseEntity.created(location).body(booking);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingRoomService.cancelBooking(id);

        return ResponseEntity.noContent().build();
    }
}
