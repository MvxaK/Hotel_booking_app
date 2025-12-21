package org.cook.booking_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String userName;
    private String email;
    private Role role;
    private List<Long> houseBookingIds;
    private List<Long> roomBookingIds;

}
