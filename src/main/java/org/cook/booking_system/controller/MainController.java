package org.cook.booking_system.controller;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HouseServiceImpl houseService;
    private final HotelServiceImpl hotelService;

    @GetMapping("/")
    public String index(Model model){
        Hotel grandHotel = hotelService.getHotelById(1L);
        Hotel TwF = hotelService.getHotelById(2L);
        House house = houseService.getHouseById(1L);

        model.addAttribute("grandHotel", grandHotel);
        model.addAttribute("TwF", TwF);
        model.addAttribute("house", house);
        return "main";
    }

    @GetMapping("/welcome")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String welcome(){
        return "index";
    }

    @GetMapping("/welcome_users")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String welcome_users(){
        return "welcome";
    }

}
