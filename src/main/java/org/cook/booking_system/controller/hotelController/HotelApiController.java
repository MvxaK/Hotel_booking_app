package org.cook.booking_system.controller.hotelController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelApiController {

    @Autowired
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

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotelToCreate){
        Hotel hotel = hotelService.createHotel(hotelToCreate);

        URI location = URI.create("/api/hotels/" + hotel.getId());

        return ResponseEntity.created(location)
                .body(hotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelToUpdate, Model model){
        Hotel hotel = hotelService.updateHotel(id, hotelToUpdate);

        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotelId(@PathVariable Long id) {
        List<Room> rooms = hotelService.getRoomsByHotelId(id);

        return ResponseEntity.ok(rooms);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id){
        hotelService.deleteHotel(id);

        return ResponseEntity.noContent()
                .build();
    }
}
