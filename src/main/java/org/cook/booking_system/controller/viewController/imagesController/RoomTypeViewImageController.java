package org.cook.booking_system.controller.viewController.imagesController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room-types/{roomTypeId}/images")
@RequiredArgsConstructor
public class RoomTypeViewImageController {

    private final RoomTypeServiceImpl roomTypeService;

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public String showRoomTypoCreateForm(@PathVariable Long roomTypeId, Model model){
        String roomTypeName = roomTypeService.getRoomTypeById(roomTypeId).getName();

        model.addAttribute("roomTypeName", roomTypeName);
        return "images/create-roomType-image";
    }
}
