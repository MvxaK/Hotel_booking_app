package org.cook.booking_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Long> roomIds;
    private List<Long> roomTypeIds;
    private List<String> imagesUrl;
}
