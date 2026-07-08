package com.rental.carflow.car;

import com.rental.carflow.location.Location;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "cars")
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarType type;

    @Column(nullable = false)
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
