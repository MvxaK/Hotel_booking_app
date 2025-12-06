package org.cook.booking_system.controller.viewController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/room-types")
@RequiredArgsConstructor
public class RoomTypeViewController {

    private final RoomTypeServiceImpl roomTypeService;
    private final HotelServiceImpl hotelService;

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showRoomTypeForm(Model model){

        return "room/create-room-type";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showRoomTypeUpdateForm(@PathVariable Long id, @RequestParam Long roomId, Model model){
        RoomType roomType = roomTypeService.getRoomTypeById(id);
        Hotel hotel = hotelService.getHotelById(roomType.getHotelId());

        model.addAttribute("roomType", roomType);
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomId", roomId);

        return "room/update-room-type";
    }

}
