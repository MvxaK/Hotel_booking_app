package org.cook.booking_system.controller;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.service_interface.HotelService;
import org.cook.booking_system.service.service_interface.HouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HouseService houseService;
    private final HotelService hotelService;

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

}
