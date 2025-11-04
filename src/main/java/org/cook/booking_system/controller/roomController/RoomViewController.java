package org.cook.booking_system.controller.roomController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.service.HotelService;
import org.cook.booking_system.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomViewController {

    private final RoomService roomService;
    private final HotelService hotelService;

    @GetMapping("/{id}")
    public String showRoomDetails(@PathVariable Long id, Model model){
        Room room = roomService.getRoomById(id);
        Hotel hotel = hotelService.getHotelById(room.getHotel().getId());
        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);

        return "room/room-details";
    }

    @GetMapping
    public String showAllRoom(Model model){
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);

        return "";
    }

    @GetMapping("/new")
    public String showCreateRoomForm(Model model){

        return "forms/create-room";
    }

}
