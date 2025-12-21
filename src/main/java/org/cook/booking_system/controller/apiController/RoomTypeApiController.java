package org.cook.booking_system.controller.apiController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.service_interface.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeApiController {

    private final RoomTypeService roomTypeService;

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

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<RoomType> getRoomTypeByIdDeletedTrue(@PathVariable Long id){
        RoomType roomType = roomTypeService.getRoomTypeByIdDeletedTrue(id);

        return ResponseEntity.ok(roomType);
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<List<RoomType>> getAllRoomTypeDeletedTrue(){
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypesDeletedTrue();

        return ResponseEntity.ok(roomTypes);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<RoomType> createRoomType(@Valid @RequestBody RoomType roomTypeToCreate){
        RoomType roomType = roomTypeService.createRoomTypeForHotel(roomTypeToCreate);

        URI location = URI.create("/api/room-types/" + roomType.getId());

        return ResponseEntity.created(location)
                .body(roomType);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<RoomType> updateRoomType(@PathVariable Long id, @Valid @RequestBody RoomType roomTypeToUpdate){
        RoomType roomType = roomTypeService.updateRoomType(id, roomTypeToUpdate);

        return ResponseEntity.ok(roomType);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id){
        roomTypeService.deleteRoomType(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> markAsDeletedRoomType(@PathVariable Long id){
        roomTypeService.markAsDeletedRoomType(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> markAsRestoredRoomType(@PathVariable Long id){
        roomTypeService.markAsRestoredRoomType(id);

        return ResponseEntity.noContent()
                .build();
    }

}
