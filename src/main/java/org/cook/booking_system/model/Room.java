package org.cook.booking_system.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.SuperBuilder;
import org.cook.booking_system.entity.HotelEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class Room extends Accommodation{
    @NotNull
    private String roomNumber;
    @NotNull
    private HotelEntity hotel;
}
