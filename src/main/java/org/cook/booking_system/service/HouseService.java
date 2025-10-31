package org.cook.booking_system.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.mapper.HouseMapper;
import org.cook.booking_system.model.House;
import org.cook.booking_system.repository.HouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService {

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final HouseMapper houseMapper;

    private final Logger logger = LoggerFactory.getLogger(HouseService.class);

    @Transactional
    public House createHouse(House house){
        HouseEntity houseEntity = houseMapper.toHouseEntity(house);
        HouseEntity saved = houseRepository.save(houseEntity);

        logger.info("The house with id-> {} is created", house.getId());
        return houseMapper.toHouseModel(saved);
    }

    @Transactional(readOnly = true)
    public List<House> getAllHouses(){
        return houseRepository.findAll().stream()
                .map(houseMapper::toHouseModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public House getHouseById(Long id){
        return houseRepository.findById(id)
                .map(houseMapper::toHouseModel)
                .orElseThrow( ()-> new EntityNotFoundException("House not found with id -> " + id));
    }

    @Transactional
    public House updateHouse(Long id, House houseToUpdate){
        HouseEntity houseEntity = houseRepository.findById(id)
                .orElseThrow( ()-> new EntityNotFoundException("House not found with id -> " + id));

        houseEntity.setName(houseToUpdate.getName());
        houseEntity.setLocation(houseToUpdate.getLocation());
        houseEntity.setCapacity(houseToUpdate.getCapacity());
        houseEntity.setPricePerNight(houseToUpdate.getPricePerNight());
        houseEntity.setAvailable(houseToUpdate.isAvailable());
        houseEntity.setImages(houseToUpdate.getImages());

        logger.info("The house with id-> {} is updated", id);
        return houseMapper.toHouseModel(houseRepository.save(houseEntity));
    }

    @Transactional
    public void deleteHouse(Long id){
        if (!houseRepository.existsById(id)){
            throw new EntityNotFoundException("House not found with id -> " + id);
        }
        logger.info("House with id -> {} is deleted", id);
        houseRepository.deleteById(id);
    }
}
