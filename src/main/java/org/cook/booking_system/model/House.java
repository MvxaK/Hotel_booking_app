package org.cook.booking_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotNull
    private int capacity;
    @NotNull
    private BigDecimal pricePerNight;
    @NotNull
    private boolean available;
    @NotNull
    private int roomsNumber;
    @NotNull
    private int bedsCount;
    @NotNull
    private int parkingSlots;
    @NotNull
    private String description;
    private List<String> imagesUrl;

}
