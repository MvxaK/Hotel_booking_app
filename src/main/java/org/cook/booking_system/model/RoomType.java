package org.cook.booking_system.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.model.images.RoomTypeImage;

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
    @NotNull
    private BigDecimal pricePerNight;
    @NotNull
    private int bedsCount;
    @NotNull
    private Long hotelId;
    private List<Long> roomIds;
    private List<RoomTypeImage> images;
}
