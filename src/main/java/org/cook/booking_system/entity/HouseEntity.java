package org.cook.booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "houses")
@SuperBuilder
@NoArgsConstructor
public class HouseEntity extends AccommodationEntity{

    @Column(name = "house_name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

}
