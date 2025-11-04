package org.cook.booking_system.model.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoom {

    private Long id;
    @NotNull
    private User user;
    @NotNull
    private RoomType roomType;
    @NotNull @FutureOrPresent
    private LocalDate checkInDate;
    @NotNull @FutureOrPresent
    private LocalDate checkOutDate;

    private BigDecimal totalPrice;
    private Status status;
}
