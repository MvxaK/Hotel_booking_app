package org.cook.booking_system.controller.bookingController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.booking.BookingHouse;
import org.cook.booking_system.service.booking.BookingHouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking/house")
@RequiredArgsConstructor
public class BookingHouseController {

    private final BookingHouseService bookingHouseService;

    @GetMapping("/{id}")
    public String showBookingDetails(@PathVariable Long id, Model model){
        BookingHouse bookingHouse = bookingHouseService.getBookingById(id);
        model.addAttribute("bookingHouse", bookingHouse);

        return "";
    }

    @GetMapping("/new_booking_house")
    public String showBookingHouseForm(){

        return "forms/create-booking-house";
    }
}
