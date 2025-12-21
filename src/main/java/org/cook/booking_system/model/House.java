package org.cook.booking_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.model.images.Image;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class House{

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull @Positive
    private int capacity;
    @NotNull @Positive
    private BigDecimal pricePerNight;
    @NotNull
    private boolean available;
    @NotNull @Positive
    private int roomsNumber;
    @NotNull @Positive
    private int bedsCount;
    @NotNull @Positive
    private int parkingSlots;
    @NotNull
    private String description;

    private List<Image> images;
    private boolean deleted;

}
