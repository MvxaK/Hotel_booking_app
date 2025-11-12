package org.cook.booking_system.controller.userController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.booking.BookingHouse;
import org.cook.booking_system.model.booking.BookingHouseDetails;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.model.booking.BookingRoomDetails;
import org.cook.booking_system.service.implementation.UserServiceImpl;
import org.cook.booking_system.service.implementation.booking.BookingHouseServiceImpl;
import org.cook.booking_system.service.implementation.booking.BookingRoomServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final BookingRoomServiceImpl bookingRoomService;
    private final BookingHouseServiceImpl bookingHouseService;

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, Model model){
        User user = userService.getUserById(id);
        List<BookingRoomDetails> bookingRooms = bookingRoomService.getAllBookingWithDetailsByUserId(id);
        List<BookingHouseDetails> bookingHouses = bookingHouseService.getAllBookingWithDetailsByUserId(id);

        model.addAttribute("user", user);
        model.addAttribute("bookingRooms", bookingRooms);
        model.addAttribute("bookingHouses", bookingHouses);

        return "user/profile";
    }
}
