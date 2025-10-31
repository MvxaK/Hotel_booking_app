package org.cook.booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Data
@Table(name = "accommodation")
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public abstract class AccommodationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "description", nullable = false, length = 10000)
    private String description;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @OneToMany(mappedBy = "accommodation", orphanRemoval = true)
    private List<ImageEntity> images;
}
