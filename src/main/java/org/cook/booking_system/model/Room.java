package org.cook.booking_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
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
    private Hotel hotel;

    @NotNull
    private RoomType roomType;
}
