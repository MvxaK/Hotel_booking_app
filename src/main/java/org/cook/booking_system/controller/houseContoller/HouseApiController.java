package org.cook.booking_system.controller.houseContoller;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/houses")
@RequiredArgsConstructor
public class HouseApiController {

    private final HouseServiceImpl houseService;

    @GetMapping
    private ResponseEntity<List<House>> getAllHouses(){
        List<House> houses = houseService.getAllHouses();

        return ResponseEntity.ok(houses);
    }

    @GetMapping("/{id}")
    private ResponseEntity<House> getHouseById(@PathVariable Long id){
        House house = houseService.getHouseById(id);

        return ResponseEntity.ok(house);
    }

    @PostMapping
    private ResponseEntity<House> createHouse(@RequestBody House houseToCreate){
        House house = houseService.createHouse(houseToCreate);

        URI location = URI.create("/api/houses/" + house.getId());

        return ResponseEntity.created(location)
                .body(house);
    }

    @PutMapping("/{id}")
    private ResponseEntity<House> updateHouse(@PathVariable Long id, @RequestBody House houseToUpdate){
        House house = houseService.updateHouse(id, houseToUpdate);

        return ResponseEntity.ok(house);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteHouse(@PathVariable Long id){
        houseService.deleteHouse(id);

        return ResponseEntity.noContent()
                .build();
    }

}
