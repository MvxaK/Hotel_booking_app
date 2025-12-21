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
@AllArgsConstructor
@NoArgsConstructor
public class RoomType {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private int capacity;
    @NotNull
    private String description;
    @NotNull @Positive
    private BigDecimal pricePerNight;
    @NotNull @Positive
    private int bedsCount;
    @NotNull
    private Long hotelId;
    private List<Long> roomIds;
    private List<Image> images;
    private boolean deleted;

}
