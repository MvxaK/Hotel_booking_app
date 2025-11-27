package org.cook.booking_system.model.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelImage {
    private Long id;
    private String url;
    private Long hotelId;
}
