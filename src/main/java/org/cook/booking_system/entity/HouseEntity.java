package org.cook.booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.entity.images.HouseImageEntity;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Data
@Table(name = "houses")
@NoArgsConstructor
@AllArgsConstructor
public class HouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "rooms_number", nullable = false)
    private int roomsNumber;

    @Column(name = "beds_count", nullable = false)
    private int bedsCount;

    @Column(name = "parking_slots", nullable = false)
    private int parkingSlots;

    @Column(name = "description", nullable = false, length = 10000)
    private String description;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseImageEntity> images;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}
