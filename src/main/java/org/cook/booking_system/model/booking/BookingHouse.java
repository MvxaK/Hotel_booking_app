package org.cook.booking_system.model.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.model.House;
import org.cook.booking_system.model.Status;
import org.cook.booking_system.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHouse {

    private Long id;
    @NotNull
    private User user;
    @NotNull
    private House house;
    @NotNull @FutureOrPresent
    private LocalDate checkInDate;
    @NotNull @FutureOrPresent
    private LocalDate checkOutDate;

    private BigDecimal totalPrice;
    private Status status;
}
