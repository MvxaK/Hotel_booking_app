package org.cook.booking_system;

import jakarta.persistence.EntityNotFoundException;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.mapper.HouseMapper;
import org.cook.booking_system.model.House;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.repository.HouseRepository;
import org.cook.booking_system.repository.booking.BookingHouseRepository;
import org.cook.booking_system.service.implementation.HouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

    @Mock
    private HouseMapper houseMapper;

    @Mock
    private  HouseRepository houseRepository;

    @Mock
    private BookingHouseRepository bookingHouseRepository;

    @InjectMocks
    private HouseServiceImpl houseService;

    private HouseEntity houseEntity;
    private House house;

    private final Long id = 42L;
    private final Long invalid_id = 67L;

    @BeforeEach
    void testData(){
        houseEntity = new HouseEntity();
        houseEntity.setId(id);
        houseEntity.setName("Luxury Cottage");
        houseEntity.setAvailable(true);
        houseEntity.setDeleted(false);

        house = new House();
        house.setId(id);
        house.setName("Luxury Cottage");
        house.setAvailable(true);
        house.setDeleted(false);
    }

    @Test
    void getAllHouses() {
        when(houseRepository.findAll()).thenReturn(List.of(houseEntity, houseEntity));
        when(houseMapper.toModel(houseEntity)).thenReturn(house);

        List<House> result = houseService.getAllHouses();

        assertEquals(2, result.size());
        verify(houseRepository).findAll();
    }

    @Test
    void getAllHousesDeletedTrue() {
        houseEntity.setDeleted(true);
        when(houseRepository.findByDeletedTrue()).thenReturn(List.of(houseEntity, houseEntity));
        when(houseMapper.toModel(houseEntity)).thenReturn(house);

        List<House> result = houseService.getAllHousesDeletedTrue();

        assertEquals(2, result.size());
        verify(houseRepository).findByDeletedTrue();
    }

    @Test
    void getHouseById() {
        when(houseRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(houseEntity));
        when(houseMapper.toModel(houseEntity)).thenReturn(house);

        House result = houseService.getHouseById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getHouseById_NotFound() {
        when(houseRepository.findByIdAndDeletedFalse(invalid_id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            houseService.getHouseById(invalid_id);
        });
    }

    @Test
    void createHouse() {
        when(houseMapper.toEntity(any(House.class))).thenReturn(houseEntity);
        when(houseRepository.save(any(HouseEntity.class))).thenReturn(houseEntity);
        when(houseMapper.toModel(any(HouseEntity.class))).thenReturn(house);

        House result = houseService.createHouse(house);

        assertNotNull(result);
        assertFalse(houseEntity.isDeleted());
        verify(houseRepository).save(houseEntity);
    }

    @Test
    void updateHouse() {
        when(houseRepository.findById(id)).thenReturn(Optional.of(houseEntity));
        when(houseRepository.save(any(HouseEntity.class))).thenReturn(houseEntity);
        when(houseMapper.toModel(any(HouseEntity.class))).thenReturn(house);

        House result = houseService.updateHouse(id, house);

        assertNotNull(result);
        verify(houseRepository).save(houseEntity);
    }

    @Test
    void updateHouse_AlreadyDeleted() {
        houseEntity.setDeleted(true);
        when(houseRepository.findById(id)).thenReturn(Optional.of(houseEntity));

        assertThrows(IllegalStateException.class, () -> {
            houseService.updateHouse(id, house);
        });
    }


    @Test
    void deleteHouse() {
        when(houseRepository.existsById(id)).thenReturn(true);
        when(bookingHouseRepository.existsActiveBookingsByHouseId(eq(id), any(), eq(Status.RESERVED))).thenReturn(false);
        houseService.deleteHouse(id);

        verify(bookingHouseRepository).deleteByHouseId(id);
        verify(houseRepository).deleteById(id);
    }

    @Test
    void deleteHouse_HasActiveBookings() {
        when(houseRepository.existsById(id)).thenReturn(true);
        when(bookingHouseRepository.existsActiveBookingsByHouseId(eq(id), any(), eq(Status.RESERVED))).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> {
            houseService.deleteHouse(id);
        });
    }

    @Test
    void markAsDeletedHouse() {
        when(houseRepository.findById(id)).thenReturn(Optional.of(houseEntity));
        when(bookingHouseRepository.existsActiveBookingsByHouseId(eq(id), any(), eq(Status.RESERVED))).thenReturn(false);
        houseService.markAsDeletedHouse(id);

        assertTrue(houseEntity.isDeleted());
        assertFalse(houseEntity.isAvailable());
        verify(houseRepository).save(houseEntity);
    }

    @Test
    void markAsDeletedHouse_AlreadyDeleted() {
        houseEntity.setDeleted(true);
        when(houseRepository.findById(id)).thenReturn(Optional.of(houseEntity));

        assertThrows(IllegalStateException.class, () -> {
            houseService.markAsDeletedHouse(id);
        });
    }

    @Test
    void markAsRestoredHouse() {
        houseEntity.setDeleted(true);
        houseEntity.setAvailable(false);
        when(houseRepository.findById(id)).thenReturn(Optional.of(houseEntity));

        houseService.markAsRestoredHouse(id);

        assertFalse(houseEntity.isDeleted());
        assertTrue(houseEntity.isAvailable());
        verify(houseRepository).save(houseEntity);
    }

    @Test
    void markAsRestoredHouse_NotDeleted() {
        houseEntity.setDeleted(false);
        when(houseRepository.findById(id)).thenReturn(Optional.of(houseEntity));

        assertThrows(IllegalStateException.class, () -> {
            houseService.markAsRestoredHouse(id);
        });
    }


}
