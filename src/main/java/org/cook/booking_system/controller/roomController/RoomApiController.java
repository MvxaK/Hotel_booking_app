package org.cook.booking_system.controller.roomController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(){
        List<Room> rooms = roomService.getAllRooms();

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id){
        Room room = roomService.getRoomById(id);

        return ResponseEntity.ok(room);
    }

    @PostMapping
    public ResponseEntity<Room> createRoomForHotel(@RequestBody Room roomToCreate) {
        Room room = roomService.createRoomForHotel(roomToCreate.getHotelId(), roomToCreate);

        URI location = URI.create("/api/rooms/" + room.getId());
        return ResponseEntity.created(location).body(room);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomToUpdate){
        Room room = roomService.updateRoom(id, roomToUpdate);

        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id){
        roomService.deleteRoom(id);

        return ResponseEntity.noContent()
                .build();
    }
}
