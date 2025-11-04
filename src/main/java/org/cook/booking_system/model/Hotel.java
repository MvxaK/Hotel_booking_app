package org.cook.booking_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private String description;
    private List<Room> rooms;
    private List<RoomType> roomTypes;

    private List<String> imagesUrl;
}
