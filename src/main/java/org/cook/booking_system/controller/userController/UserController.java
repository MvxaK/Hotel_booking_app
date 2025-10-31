package org.cook.booking_system.controller.userController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Booking;
import org.cook.booking_system.model.User;
import org.cook.booking_system.service.BookingService;
import org.cook.booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, Model model){
        User user = userService.getUserById(id);
        List<Booking> bookings = bookingService.getAllBookingByUserId(id);

        model.addAttribute("user", user);
        model.addAttribute("bookings", bookings);

        return "";
    }

    @GetMapping("/new")
    public String showUserCreateForm(){
        return "";
    }
}
