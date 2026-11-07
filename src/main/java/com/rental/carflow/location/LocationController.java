package com.rental.carflow.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Locations", description = "Endpoints for managing pickup and return stations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new station location")
    public LocationDTO createLocation(@Valid @RequestBody CreateLocationRequest request) {
        return locationService.createLocation(request);
    }

    @GetMapping
    @Operation(summary = "Get all station locations")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get station location by ID")
    public LocationDTO getLocationById(@PathVariable UUID id) {
        return locationService.getLocationById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete station location by ID")
    public void deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocationById(id);
    }
}
