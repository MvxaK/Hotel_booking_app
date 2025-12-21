package org.cook.booking_system.controller.apiController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.House;
import org.cook.booking_system.service.service_interface.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/houses")
@RequiredArgsConstructor
public class HouseApiController {

    private final HouseService houseService;

    @GetMapping
    public ResponseEntity<List<House>> getAllHouses(){
        List<House> houses = houseService.getAllHouses();

        return ResponseEntity.ok(houses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<House> getHouseById(@PathVariable Long id){
        House house = houseService.getHouseById(id);

        return ResponseEntity.ok(house);
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<List<House>> getAllHousesDeletedTrue(){
        List<House> houses = houseService.getAllHousesDeletedTrue();

        return ResponseEntity.ok(houses);
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<House> getHouseByIdDeletedTrue(@PathVariable Long id){
        House house = houseService.getHouseByIdDeletedTrue(id);

        return ResponseEntity.ok(house);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<House> createHouse(@Valid  @RequestBody House houseToCreate){
        House house = houseService.createHouse(houseToCreate);

        URI location = URI.create("/api/houses/" + house.getId());

        return ResponseEntity.created(location)
                .body(house);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<House> updateHouse(@PathVariable Long id, @Valid @RequestBody House houseToUpdate){
        House house = houseService.updateHouse(id, houseToUpdate);

        return ResponseEntity.ok(house);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id){
        houseService.deleteHouse(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<Void> markAsDeletedHouse(@PathVariable Long id){
        houseService.markAsDeletedHouse(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOUSE_KEEPER')")
    public ResponseEntity<Void> markAsRestoredHouse(@PathVariable Long id){
        houseService.markAsRestoredHouse(id);

        return ResponseEntity.noContent()
                .build();
    }

}
