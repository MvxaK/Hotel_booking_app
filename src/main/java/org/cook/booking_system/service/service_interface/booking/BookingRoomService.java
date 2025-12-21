package org.cook.booking_system.service.service_interface.booking;

import org.cook.booking_system.model.booking.BookingRoomDetails;
import org.cook.booking_system.model.booking.BookingRoomRequest;
import org.cook.booking_system.model.booking.BookingRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingRoomService {

    BookingRoom createBookingForUser(Long userId, BookingRoomRequest bookingRequest);
    BookingRoom getBookingById(Long id);
    List<BookingRoom> getAllBookingByUserId(Long userId);
    List<BookingRoomDetails> getAllBookingWithDetailsByUserId(Long userId);
    void cancelBooking(Long bookingId);
    void deleteBooking(Long bookingId);

}
