package org.cook.booking_system.controller.roomController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.service.RoomTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room-type")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/new")
    public String showRoomTypeForm(){

        return "";
    }

}
