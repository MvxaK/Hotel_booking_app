package org.cook.booking_system.controller.userController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.User;
import org.cook.booking_system.model.booking.BookingHouseDetails;
import org.cook.booking_system.model.booking.BookingRoomDetails;
import org.cook.booking_system.service.implementation.UserServiceImpl;
import org.cook.booking_system.service.implementation.booking.BookingHouseServiceImpl;
import org.cook.booking_system.service.implementation.booking.BookingRoomServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/my-profile")
    public String showMyProfile(Authentication authentication, Model model){
        if(authentication == null){
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.getUserByUserName(username);

        List<BookingRoomDetails> bookingRooms = bookingRoomService.getAllBookingWithDetailsByUserId(user.getId());
        List<BookingHouseDetails> bookingHouses = bookingHouseService.getAllBookingWithDetailsByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("bookingRooms", bookingRooms);
        model.addAttribute("bookingHouses", bookingHouses);

        return "user/profile";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
