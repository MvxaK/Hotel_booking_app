package org.cook.booking_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Long accommodationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
