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
    Hotel getHotelByIdDeletedTrue(Long id);

    Hotel getHotelByIdIncludeDeleted(Long id);
    List<Hotel> getAllHotels();
    List<Hotel> getAllHotelsDeletedTrue();
    List<Room> getRoomsByHotelId(Long id);
    List<Room> getRoomsByHotelIdDeletedTrue(Long id);
    List<RoomType> getRoomTypesByHotelId(Long id);
    List<RoomType> getRoomTypesByHotelIdDeletedTrue(Long id);
    Hotel updateHotel(Long id, Hotel hotelToUpdate);
    void deleteHotel(Long id);
    void markAsDeletedHotel(Long id);
    void markAsRestoredHotel(Long id);

}