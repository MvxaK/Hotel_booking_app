package org.cook.booking_system.controller.apiController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.service.service_interface.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<List<Room>> getAllRoomsDeletedTrue(){
        List<Room> rooms = roomService.getAllRoomsDeletedTrue();

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Room> getRoomByIdDeletedTrue(@PathVariable Long id){
        Room room = roomService.getRoomByIdDeletedTrue(id);

        return ResponseEntity.ok(room);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Room> createRoomForHotel(@Valid @RequestBody Room roomToCreate) {
        Room room = roomService.createRoomForHotel(roomToCreate);

        URI location = URI.create("/api/rooms/" + room.getId());

        return ResponseEntity.created(location)
                .body(room);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @Valid  @RequestBody Room roomToUpdate){
        Room room = roomService.updateRoom(id, roomToUpdate);

        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id){
        roomService.deleteRoom(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> markAsDeletedRoom(@PathVariable Long id){
        roomService.markAsDeletedRoom(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> markAsRestoredRoom(@PathVariable Long id){
        roomService.markAsRestoredRoom(id);

        return ResponseEntity.noContent()
                .build();
    }
}
