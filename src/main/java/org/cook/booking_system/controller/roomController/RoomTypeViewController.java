package org.cook.booking_system.controller.roomController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room-types")
@RequiredArgsConstructor
public class RoomTypeViewController {

    private final RoomTypeServiceImpl roomTypeService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showRoomTypeForm(){

        return "";
    }

}
