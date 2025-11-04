package org.cook.booking_system.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.model.booking.BookingHouse;
import org.cook.booking_system.model.booking.BookingRoom;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Null
    private Long id;
    @NotNull
    private String userName;
    @NotNull
    private String email;

    private List<BookingHouse> houseBookings;
    private List<BookingRoom> roomBookings;

    @NotNull
    private Set<Role> roles;
}
