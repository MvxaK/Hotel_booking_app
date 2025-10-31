package org.cook.booking_system.controller.hotelController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelViewController {

    @Autowired
    private final HotelService hotelService;

    @GetMapping
    public String showAllHotels(Model model){
        List<Hotel> hotels = hotelService.getAllHotels();
        model.addAttribute("hotels", hotels);

        return "hotel/hotels";
    }

    @GetMapping("/{id}")
    public String showHotelDetails(@PathVariable Long id, Model model){
        Hotel hotel = hotelService.getHotelById(id);
        List<Room> rooms = hotelService.getRoomsByHotelId(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);

        return "hotel/hotel-details";
    }

    @GetMapping("/new")
    public String showCreateHotelForm(){

        return "forms/create-hotel";
    }

}
