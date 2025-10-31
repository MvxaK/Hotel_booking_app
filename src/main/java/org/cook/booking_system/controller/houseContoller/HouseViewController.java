package org.cook.booking_system.controller.houseContoller;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseViewController {

    @Autowired
    private final HouseService houseService;

    @GetMapping("/")
    public String showAllHouses(Model model){
        List<House> houses = houseService.getAllHouses();
        model.addAttribute("houses", houses);

        return "";
    }

    @GetMapping("/{id}")
    public String showHouseDetails(@PathVariable Long id, Model model){
        House house = houseService.getHouseById(id);
        model.addAttribute("house", house);

        return "house/house-details";
    }

    @GetMapping("/new")
    public String showHouseCreateForm(){

        return "forms/create-house";
    }

}
