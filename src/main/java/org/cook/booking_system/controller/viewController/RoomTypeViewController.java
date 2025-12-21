package org.cook.booking_system.controller.viewController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
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

import java.util.List;

@Controller
@RequestMapping("/room-types")
@RequiredArgsConstructor
public class RoomTypeViewController {

    private final RoomTypeServiceImpl roomTypeService;
    private final HotelServiceImpl hotelService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomTypes(Model model){
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypes();

        model.addAttribute("roomTypes", roomTypes);
        return "room/room-types";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomTypeDetails(@PathVariable Long id, Model model){
        RoomType roomType = roomTypeService.getRoomTypeById(id);

        model.addAttribute("roomType", roomType);
        return "room/room-type-details";
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomTypesDeleted(Model model){
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypesDeletedTrue();

        model.addAttribute("roomTypes", roomTypes);
        return "room/room-types";
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomTypeDetailsDeleted(@PathVariable Long id, Model model){
        RoomType roomType = roomTypeService.getRoomTypeByIdDeletedTrue(id);

        model.addAttribute("roomType", roomType);
        return "room/room-type-details";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomTypeCreateForm(Model model){

        return "room/create-room-type";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomTypeUpdateForm(@PathVariable Long id, Model model){
        RoomType roomType = roomTypeService.getRoomTypeById(id);
        Hotel hotel = hotelService.getHotelById(roomType.getHotelId());

        model.addAttribute("roomType", roomType);
        model.addAttribute("hotel", hotel);

        return "room/update-room-type";
    }

}
