package org.cook.booking_system.controller.roomController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.RoomServiceImpl;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomViewController {

    private final RoomServiceImpl roomService;
    private final HotelServiceImpl hotelService;
    private final RoomTypeServiceImpl roomTypeService;

    @GetMapping("/{id}")
    public String showRoomDetails(@PathVariable Long id, Model model){
        Room room = roomService.getRoomById(id);
        Hotel hotel = hotelService.getHotelById(room.getHotelId());
        RoomType roomType = roomTypeService.getRoomTypeById(room.getRoomTypeId());
        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomType", roomType);

        return "room/room-details";
    }

    @GetMapping("/new")
    public String showCreateRoomForm(Model model){

        return "forms/create-room";
    }

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        Hotel hotel = hotelService.getHotelById(room.getHotelId());
        RoomType roomType = roomTypeService.getRoomTypeById(room.getRoomTypeId());

        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomType", roomType);

        return "forms/update-room";
    }
}
