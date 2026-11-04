package com.rental.carflow.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    boolean existsByName(String name);
    Optional<Location> findByName(String name);
    Optional<Location> findByUUID(UUID uuid);
}
