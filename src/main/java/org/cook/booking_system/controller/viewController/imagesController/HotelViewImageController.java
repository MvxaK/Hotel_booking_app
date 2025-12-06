package org.cook.booking_system.controller.viewController.imagesController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.service.implementation.HotelServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hotels/{hotelId}/images")
@RequiredArgsConstructor
public class HotelViewImageController {

    private final HotelServiceImpl hotelService;

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showHouseCreateForm(@PathVariable Long hotelId, Model model){
        String hotelName = hotelService.getHotelById(hotelId).getName();

        model.addAttribute("hotelName", hotelName);
        return "images/create-hotel-image";
    }

}
