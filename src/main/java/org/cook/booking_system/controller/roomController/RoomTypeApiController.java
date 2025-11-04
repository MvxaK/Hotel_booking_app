package org.cook.booking_system.controller.roomController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/room-type")
@RequiredArgsConstructor
public class RoomTypeApiController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Long id){
        RoomType roomType = roomTypeService.getById(id);

        return ResponseEntity.ok(roomType);
    }

    @GetMapping
    public ResponseEntity<List<RoomType>> getAllRoomType(){
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypes();

        return ResponseEntity.ok(roomTypes);
    }

    @PostMapping
    public ResponseEntity<RoomType> createRoomType(RoomType roomTypeToCreate){
        RoomType roomType = roomTypeService.create(roomTypeToCreate);

        URI location = URI.create("/api/room-type/" + roomType.getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomType> updateRoomType(@PathVariable Long id, RoomType roomTypeToUpdate){
        RoomType roomType = roomTypeService.update(id, roomTypeToUpdate);

        return ResponseEntity.ok(roomType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id){
        roomTypeService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
