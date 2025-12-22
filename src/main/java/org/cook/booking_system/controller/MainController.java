package org.cook.booking_system.controller;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.service_interface.HotelService;
import org.cook.booking_system.service.service_interface.HouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HouseService houseService;
    private final HotelService hotelService;

    @GetMapping("/")
    public String index(Model model){
        List<Hotel> popularHotels = hotelService.getAllHotels();
        List<House> popularHouses = houseService.getAllHouses();

        model.addAttribute("grandHotel", popularHotels.size() > 0 ? popularHotels.get(0) : null);
        model.addAttribute("TwF", popularHotels.size() > 1 ? popularHotels.get(1) : null);
        model.addAttribute("house", popularHouses.size() > 0 ? popularHouses.getFirst() : null);

        return "main";
    }

}
