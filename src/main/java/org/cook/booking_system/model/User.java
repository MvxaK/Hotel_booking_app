package org.cook.booking_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotNull
    private Set<Role> roles;
    private List<Long> houseBookingIds;
    private List<Long> roomBookingIds;

}
