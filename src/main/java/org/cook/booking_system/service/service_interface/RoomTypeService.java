package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomTypeService {

    List<RoomType> getAllRoomTypes();
    RoomType getRoomTypeById(Long id);
    List<Room> getRoomsByRoomTypeId(Long id);
    RoomType createRoomType(RoomType roomType);
    RoomType updateRoomType(Long id, RoomType updated);
    void deleteRoomType(Long id);
}