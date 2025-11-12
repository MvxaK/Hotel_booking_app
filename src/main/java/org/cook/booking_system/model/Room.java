package org.cook.booking_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    private Long id;
    @NotNull
    private String roomNumber;
    @NotNull
    private boolean available;

    @NotNull
    private Long hotelId;
    @NotNull
    private Long roomTypeId;
}
