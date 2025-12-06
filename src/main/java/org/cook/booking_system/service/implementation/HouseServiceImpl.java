package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.mapper.HouseMapper;
import org.cook.booking_system.model.House;
import org.cook.booking_system.repository.HouseRepository;
import org.cook.booking_system.service.service_interface.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService{

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    @Transactional
    public House createHouse(House house){
        HouseEntity houseEntity = houseMapper.toEntity(house);
        HouseEntity saved = houseRepository.save(houseEntity);

        return houseMapper.toModel(saved);
    }

    @Transactional(readOnly = true)
    public List<House> getAllHouses(){
        return houseRepository.findAll().stream()
                .map(houseMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public House getHouseById(Long id){
        return houseRepository.findById(id)
                .map(houseMapper::toModel)
                .orElseThrow( ()-> new EntityNotFoundException("House not found with id -> " + id));
    }

    @Transactional
    public House updateHouse(Long id, House houseToUpdate){
        HouseEntity houseEntity = houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + id));

        houseEntity.setName(houseToUpdate.getName());
        houseEntity.setLocation(houseToUpdate.getLocation());
        houseEntity.setCapacity(houseToUpdate.getCapacity());
        houseEntity.setPricePerNight(houseToUpdate.getPricePerNight());
        houseEntity.setAvailable(houseToUpdate.isAvailable());
        houseEntity.setRoomsNumber(houseToUpdate.getRoomsNumber());
        houseEntity.setBedsCount(houseToUpdate.getBedsCount());
        houseEntity.setParkingSlots(houseToUpdate.getParkingSlots());
        houseEntity.setDescription(houseToUpdate.getDescription());

        return houseMapper.toModel(houseRepository.save(houseEntity));
    }

    @Transactional
    public void deleteHouse(Long id){
        if (!houseRepository.existsById(id)){
            throw new EntityNotFoundException("House not found with id -> " + id);
        }
        houseRepository.deleteById(id);
    }
}
