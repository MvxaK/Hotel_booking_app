package org.cook.booking_system.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cook.booking_system.entity.ImageEntity;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Accommodation {

    private Long id;
    @NotNull
    private int capacity;
    @NotNull
    private BigDecimal pricePerNight;
    @NotNull
    private boolean available;
    private List<ImageEntity> images;

}
