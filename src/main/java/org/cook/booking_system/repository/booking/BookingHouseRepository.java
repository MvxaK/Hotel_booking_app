package org.cook.booking_system.repository.booking;

import org.cook.booking_system.entity.booking.BookingHouseEntity;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.booking.BookingHouseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingHouseRepository extends JpaRepository<BookingHouseEntity, Long> {
    List<BookingHouseEntity> findByUserId(Long userId);
    List<BookingHouseEntity> findByHouseIdAndStatus(Long houseId, Status status);

    @Query("select new org.cook.booking_system.model.booking.BookingHouseDetails(b.id, b.user.id, hs.name, hs.location, b.checkInDate, b.checkOutDate, b.totalPrice, b.status) from BookingHouseEntity b join b.house hs where b.user.id = :userId")
    List<BookingHouseDetails> findBookingDetailsByUserId(@Param("userId") Long userId);

    @Query("select case when count(b) > 0 then true else false end from BookingHouseEntity b where b.house.id = :houseId and b.checkOutDate >= :currentDate and b.status = :status")
    boolean existsActiveBookingsByHouseId(@Param("houseId") Long houseId, @Param("currentDate") LocalDate currentDate, @Param("status") Status status);

    @Modifying
    @Transactional
    @Query("delete from BookingHouseEntity b where b.house.id = :id")
    void deleteByHouseId(Long id);
}
