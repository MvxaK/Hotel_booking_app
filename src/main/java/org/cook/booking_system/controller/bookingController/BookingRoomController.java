package org.cook.booking_system.controller.bookingController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.service.booking.BookingRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking/room")
@RequiredArgsConstructor
public class BookingRoomController {

    private final BookingRoomService bookingRoomService;

    @GetMapping("/{id}")
    public String showBookingDetails(@PathVariable Long id, Model model){
        BookingRoom bookingRoom = bookingRoomService.getBookingById(id);
        model.addAttribute("bookingRoom", bookingRoom);

        return "";
    }

    @GetMapping("/new_booking_room")
    public String showBookingHouseForm(){

        return "forms/create-booking-room";
    }
}
