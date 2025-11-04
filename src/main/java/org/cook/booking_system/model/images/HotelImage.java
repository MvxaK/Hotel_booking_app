package org.cook.booking_system.model.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.model.Hotel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelImage {
    private Long id;
    private String url;
    private Long hotelId;
}
