package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.Room;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {

    Room createRoomForHotel(Long hotelId, Room room);
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    Room updateRoom(Long id, Room roomToUpdate);
    void deleteRoom(Long id);
}