package org.cook.booking_system.entity.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.entity.HouseEntity;
import org.cook.booking_system.entity.RoomTypeEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "booking_house")
@NoArgsConstructor
@AllArgsConstructor
public class BookingHouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    private HouseEntity house;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
