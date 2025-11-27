package org.cook.booking_system.controller.hotelController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelViewController {

    private final HotelServiceImpl hotelService;

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
        List<RoomType> roomTypes = hotelService.getRoomTypesByHotelId(id);

        Map<Long, RoomType> roomTypeMap = roomTypes.stream()
                        .collect(Collectors.toMap(RoomType::getId, x -> x));

        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomTypeMap", roomTypeMap);

        return "hotel/hotel-details";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateHotelForm(){

        return "forms/create-hotel";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateHotelForm(@PathVariable Long id, Model model){
        Hotel hotel = hotelService.getHotelById(id);
        model.addAttribute("hotel", hotel);

        return "forms/update-hotel";
    }

}
