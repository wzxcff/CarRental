package com.rental.carflow.location;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "locations")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;
}
