package org.cook.booking_system.controller.viewController.bookingsController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.service.implementation.booking.BookingRoomServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookings/rooms")
@RequiredArgsConstructor
public class BookingRoomController {

    private final BookingRoomServiceImpl bookingRoomService;

    @GetMapping("/{id}")
    public String showBookingDetails(@PathVariable Long id, Model model){
        BookingRoom bookingRoom = bookingRoomService.getBookingById(id);
        model.addAttribute("bookingRoom", bookingRoom);

        return "";
    }

    @GetMapping("/new")
    public String showBookingHouseForm(){

        return "booking/create-booking-room";
    }
}
