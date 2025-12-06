package org.cook.booking_system.controller.apiController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.implementation.RoomTypeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeApiController {

    private final RoomTypeServiceImpl roomTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Long id){
        RoomType roomType = roomTypeService.getRoomTypeById(id);

        return ResponseEntity.ok(roomType);
    }

    @GetMapping
    public ResponseEntity<List<RoomType>> getAllRoomType(){
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypes();

        return ResponseEntity.ok(roomTypes);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomType> createRoomType(@RequestBody RoomType roomTypeToCreate){
        RoomType roomType = roomTypeService.createRoomType(roomTypeToCreate);

        URI location = URI.create("/api/room-types/" + roomType.getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomType> updateRoomType(@PathVariable Long id, @RequestBody RoomType roomTypeToUpdate){
        RoomType roomType = roomTypeService.updateRoomType(id, roomTypeToUpdate);

        return ResponseEntity.ok(roomType);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id){
        roomTypeService.deleteRoomType(id);

        return ResponseEntity.noContent().build();
    }

}
