package org.cook.booking_system.controller.viewController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseViewController {

    private final HouseServiceImpl houseService;

    @GetMapping
    public String showAllHouses(Model model){
        List<House> houses = houseService.getAllHouses();
        model.addAttribute("houses", houses);

        return "house/houses";
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public String showAllHousesDeletedTrue(Model model){
        List<House> houses = houseService.getAllHousesDeletedTrue();
        model.addAttribute("houses", houses);

        return "house/houses";
    }

    @GetMapping("/{id}")
    public String showHouseDetails(@PathVariable Long id, Model model){
        House house = houseService.getHouseById(id);
        model.addAttribute("house", house);

        return "house/house-details";
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public String showHouseDetailsDeletedTrue(@PathVariable Long id, Model model){
        House house = houseService.getHouseByIdDeletedTrue(id);
        model.addAttribute("house", house);

        return "house/house-details";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public String showHouseCreateForm(){

        return "house/create-house";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public String showHouseUpdateForm(@PathVariable Long id, Model model){
        House house = houseService.getHouseById(id);
        model.addAttribute("house", house);

        return "house/update-house";
    }
}
