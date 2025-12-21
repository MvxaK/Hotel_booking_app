package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.House;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseService {

    House createHouse(House house);
    List<House> getAllHouses();
    List<House> getAllHousesDeletedTrue();
    House getHouseById(Long id);
    House getHouseByIdDeletedTrue(Long id);
    House updateHouse(Long id, House houseToUpdate);
    void deleteHouse(Long id);
    void markAsDeletedHouse(Long id);
    void markAsRestoredHouse(Long id);
}
