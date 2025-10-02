package org.cook.booking_system.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Booking {
    @Null
    private Long id;
    @NotNull
    private User user;
    @NotNull
    private Accommodation accommodation;
    @NotNull @FutureOrPresent
    private LocalDate checkInDate;
    @NotNull @FutureOrPresent
    private LocalDate checkOutDate;

    private BigDecimal totalPrice;
    private Status status;
}
