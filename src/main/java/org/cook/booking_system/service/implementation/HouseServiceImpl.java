package org.cook.booking_system.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.mapper.HouseMapper;
import org.cook.booking_system.model.House;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.repository.HouseRepository;
import org.cook.booking_system.repository.booking.BookingHouseRepository;
import org.cook.booking_system.service.service_interface.HouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService{

    private final HouseMapper houseMapper;
    private final HouseRepository houseRepository;
    private final BookingHouseRepository bookingHouseRepository;

    @Transactional(readOnly = true)
    public List<House> getAllHouses(){
        return houseRepository.findAll().stream()
                .map(houseMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<House> getAllHousesDeletedTrue() {
        return houseRepository.findByDeletedTrue().stream()
                .map(houseMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public House getHouseById(Long id){
        return houseRepository.findByIdAndDeletedFalse(id)
                .map(houseMapper::toModel)
                .orElseThrow( ()-> new EntityNotFoundException("House not found or marked as deleted with id -> " + id));
    }

    @Transactional(readOnly = true)
    public House getHouseByIdDeletedTrue(Long id) {
        return houseRepository.findByIdAndDeletedTrue(id)
                .map(houseMapper::toModel)
                .orElseThrow( ()-> new EntityNotFoundException("Deleted House not found with id -> " + id));
    }

    @Transactional
    public House createHouse(House house){
        HouseEntity houseEntity = houseMapper.toEntity(house);
        houseEntity.setDeleted(false);

        return houseMapper.toModel(houseRepository.save(houseEntity));
    }

    @Transactional
    public House updateHouse(Long id, House houseToUpdate){
        HouseEntity houseEntity = houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + id));
        
        if(houseEntity.isDeleted()){
            throw new IllegalStateException("Cannot update house that marked as deleted");
        }

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

        boolean hasActiveBookings = bookingHouseRepository.existsActiveBookingsByHouseId(id, LocalDate.now(), Status.RESERVED);
        if (hasActiveBookings) {
            throw new IllegalStateException("Cannot delete house with active or future bookings.");
        }

        bookingHouseRepository.deleteByHouseId(id);
        houseRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeletedHouse(Long id){
        HouseEntity houseEntity = houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + id));

        if(houseEntity.isDeleted()){
            throw new IllegalStateException("Cannot delete house that already marked as deleted");
        }

        boolean hasActiveBookings = bookingHouseRepository.existsActiveBookingsByHouseId(id, LocalDate.now(), Status.RESERVED);
        if (hasActiveBookings) {
            throw new IllegalStateException("Cannot delete house with active or future bookings.");
        }
        
        houseEntity.setDeleted(true);
        houseEntity.setAvailable(false);

        houseRepository.save(houseEntity);
    }

    @Transactional
    public void markAsRestoredHouse(Long id){
        HouseEntity houseEntity = houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id -> " + id));

        if (!houseEntity.isDeleted()) {
            throw new IllegalStateException("Cannot restore house that already restored");
        }

        houseEntity.setDeleted(false);
        houseEntity.setAvailable(true);

        houseRepository.save(houseEntity);
    }

}
