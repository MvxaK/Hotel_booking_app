package org.cook.booking_system.controller.userController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.booking.BookingHouse;
import org.cook.booking_system.model.booking.BookingRoom;
import org.cook.booking_system.service.booking.BookingHouseService;
import org.cook.booking_system.service.booking.BookingRoomService;
import org.cook.booking_system.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingRoomService bookingRoomService;
    private final BookingHouseService bookingHouseService;

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, Model model){
        User user = userService.getUserById(id);
        List<BookingRoom> bookingRooms = bookingRoomService.getAllBookingByUserId(id);
        List<BookingHouse> bookingHouses = bookingHouseService.getAllBookingByUserId(id);

        model.addAttribute("user", user);
        model.addAttribute("bookingRooms", bookingRooms);
        model.addAttribute("bookingHouses", bookingHouses);

        return "";
    }

    @GetMapping("/new")
    public String showUserCreateForm(){
        return "";
    }
}
