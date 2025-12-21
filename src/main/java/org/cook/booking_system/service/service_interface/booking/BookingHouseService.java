package org.cook.booking_system.service.service_interface.booking;

import org.cook.booking_system.model.booking.BookingHouseDetails;
import org.cook.booking_system.model.booking.BookingHouseRequest;
import org.cook.booking_system.model.booking.BookingHouse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingHouseService {

    BookingHouse createBookingForUser(Long userId, BookingHouseRequest bookingRequest);
    BookingHouse getBookingById(Long id);
    List<BookingHouse> getAllBookingByUserId(Long userId);
    List<BookingHouseDetails> getAllBookingWithDetailsByUserId(Long userId);
    void cancelBooking(Long bookingId);
    void deleteBooking(Long bookingId);
}
