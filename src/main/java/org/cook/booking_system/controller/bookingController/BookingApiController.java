package org.cook.booking_system.controller.bookingController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Booking;
import org.cook.booking_system.model.BookingRequest;
import org.cook.booking_system.model.User;
import org.cook.booking_system.service.BookingService;
import org.cook.booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingApiController {

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id){
        Booking booking = bookingService.getBookingById(id);

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<Booking>> getAllBookingBuUserId(@PathVariable Long userId){
        List<Booking> bookings = bookingService.getAllBookingByUserId(userId);

        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<Booking> createBookingForUser(@RequestBody BookingRequest bookingRequest, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserName(userDetails.getUsername());

        Booking booking = bookingService.createBookingForUser(user.getId(), bookingRequest);

        URI location = URI.create("/api/booking/" + booking.getId());

        return ResponseEntity.created(location)
                .body(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingToUpdate){
        Booking booking = bookingService.updateBooking(id, bookingToUpdate);

        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id){
        bookingService.deleteById(id);

        return ResponseEntity.noContent()
                .build();
    }

}
