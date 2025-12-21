package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.Room;
import org.cook.booking_system.model.RoomType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomTypeService {

    List<RoomType> getAllRoomTypes();
    List<RoomType> getAllRoomTypesDeletedTrue();
    List<RoomType> findAllIncludeDeleted();
    RoomType getRoomTypeById(Long id);
    RoomType getRoomTypeByIdDeletedTrue(Long id);
    RoomType getRoomTypeByIdIncludeDeleted(Long id);
    List<Room> getRoomsByRoomTypeId(Long id);
    RoomType createRoomTypeForHotel(RoomType roomType);
    RoomType updateRoomType(Long id, RoomType updated);
    void deleteRoomType(Long id);
    void markAsDeletedRoomType(Long id);
    void markAsRestoredRoomType(Long id);
}