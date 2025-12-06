package org.cook.booking_system.controller.viewController.imagesController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/houses/{houseId}/images")
@RequiredArgsConstructor
public class HouseViewImageController {

    private final HouseServiceImpl houseService;

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showHouseCreateForm(@PathVariable Long houseId, Model model){
        String houseName = houseService.getHouseById(houseId).getName();

        model.addAttribute("houseName", houseName);
        return "images/create-house-image";
    }

}
