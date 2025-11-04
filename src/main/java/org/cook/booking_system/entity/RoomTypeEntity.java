package org.cook.booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.booking_system.entity.images.RoomTypeImageEntity;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "description", nullable = false, length = 10000)
    private String description;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "beds_count", nullable = false)
    private int bedsCount;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private HotelEntity hotel;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RoomEntity> rooms;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RoomTypeImageEntity> images;
}
