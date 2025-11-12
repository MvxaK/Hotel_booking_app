package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.Hotel;
import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HotelService {

    Hotel createHotel(Hotel hotel);
    Hotel getHotelById(Long id);
    List<Hotel> getAllHotels();
    List<Room> getRoomsByHotelId(Long id);
    List<RoomType> getRoomTypesByHotelId(Long id);
    Hotel updateHotel(Long id, Hotel hotelToUpdate);
    void deleteHotel(Long id);
}