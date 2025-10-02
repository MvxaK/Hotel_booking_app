package org.cook.booking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.cook.booking_system.entity.ImageEntity;
import org.cook.booking_system.entity.RoomEntity;

import java.util.List;

@Data
@AllArgsConstructor
public class Hotel {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    private List<RoomEntity> rooms;
    private List<ImageEntity> images;
}
