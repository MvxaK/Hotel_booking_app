package org.cook.booking_system.controller.bookingController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Booking;
import org.cook.booking_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public String showBookingDetails(@PathVariable Long id, Model model){
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);

        return "";
    }

    @GetMapping("/new_booking_room")
    public String showBookingCreateFormRoom(){

        return "forms/create-booking-room";
    }

    @GetMapping("/new_booking_house")
    public String showBookingCreateFormHouse(){

        return "forms/create-booking-house";
    }
}
