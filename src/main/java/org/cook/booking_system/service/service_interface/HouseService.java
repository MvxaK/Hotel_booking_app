package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.model.House;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseService {

    House createHouse(House house);
    List<House> getAllHouses();
    House getHouseById(Long id);
    House updateHouse(Long id, House houseToUpdate);
    void deleteHouse(Long id);
}
