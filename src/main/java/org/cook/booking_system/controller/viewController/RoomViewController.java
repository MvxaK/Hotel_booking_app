package org.cook.booking_system.controller.viewController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.cook.booking_system.service.implementation.RoomServiceImpl;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomViewController {

    private final RoomServiceImpl roomService;
    private final HotelServiceImpl hotelService;
    private final RoomTypeServiceImpl roomTypeService;

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showDeletedRooms(Model model){
        List<Room> rooms = roomService.getAllRoomsDeletedTrue();
        List<RoomType> roomTypes = roomTypeService.getAllIncludeDeleted();

        Map<Long, RoomType> roomTypeMap = roomTypes.stream()
                .collect(Collectors.toMap(RoomType::getId, roomType -> roomType));

        model.addAttribute("rooms", rooms);
        model.addAttribute("roomTypeMap", roomTypeMap);

        return "room/deleted-rooms";
    }

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

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomDetailsDeletedTrue(@PathVariable Long id, Model model){
        Room room = roomService.getRoomByIdDeletedTrue(id);
        Hotel hotel = hotelService.getHotelByIdIncludeDeleted(room.getHotelId());
        RoomType roomType = roomTypeService.getRoomTypeByIdIncludeDeleted(room.getRoomTypeId());

        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomType", roomType);

        return "room/room-details";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showCreateRoomForm(Model model){

        return "room/create-room";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        Hotel hotel = hotelService.getHotelById(room.getHotelId());
        RoomType roomType = roomTypeService.getRoomTypeById(room.getRoomTypeId());

        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomType", roomType);

        return "room/update-room";
    }
}
