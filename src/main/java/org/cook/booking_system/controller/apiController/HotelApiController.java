package org.cook.booking_system.controller.apiController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.cook.booking_system.service.service_interface.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelApiController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels(){
        List<Hotel> hotels = hotelService.getAllHotels();

        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id){
        Hotel hotel = hotelService.getHotelById(id);

        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<List<Hotel>> getAllHotelsDeletedTrue(){
        List<Hotel> hotels = hotelService.getAllHotelsDeletedTrue();

        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Hotel> getHotelByIdDeletedTrue(@PathVariable Long id){
        Hotel hotel = hotelService.getHotelByIdDeletedTrue(id);

        return ResponseEntity.ok(hotel);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotelToCreate){
        Hotel hotel = hotelService.createHotel(hotelToCreate);

        URI location = URI.create("/api/hotels/" + hotel.getId());

        return ResponseEntity.created(location)
                .body(hotel);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @Valid @RequestBody Hotel hotelToUpdate){
        Hotel hotel = hotelService.updateHotel(id, hotelToUpdate);

        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotelId(@PathVariable Long id) {
        List<Room> rooms = hotelService.getRoomsByHotelId(id);

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}/room-types")
    public ResponseEntity<List<RoomType>> getRoomTypesByHotelId(@PathVariable Long id) {
        List<RoomType> roomTypes = hotelService.getRoomTypesByHotelId(id);

        return ResponseEntity.ok(roomTypes);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id){
        hotelService.deleteHotel(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> markAsDeletedHotel(@PathVariable Long id){
        hotelService.markAsDeletedHotel(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_HOTEL_KEEPER')")
    public ResponseEntity<Void> markAsRestoredHotel(@PathVariable Long id){
        hotelService.markAsRestoredHotel(id);

        return ResponseEntity.noContent()
                .build();
    }

}
