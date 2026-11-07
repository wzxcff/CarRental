package com.rental.carflow.location;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public LocationDTO createLocation(CreateLocationRequest request) {
        if (locationRepository.existsByName(request.name())) {
            throw new RuntimeException("Location with name " + request.name() + " already exists"); // TODO: Implement ConflictException
        }

        Location location = new Location(null, request.name(), request.address());
        Location savedLocation = locationRepository.save(location);
        return new LocationDTO(savedLocation.getId(), savedLocation.getName(), savedLocation.getAddress());
    }

    public LocationDTO getLocationById(UUID uuid) {
        Location location = locationRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Location not found with id " + uuid)); // TODO: Implement ResourceNotFoundException
        return new LocationDTO(location.getId(), location.getName(), location.getAddress());
    }

    public List<LocationDTO> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream().map(location -> new LocationDTO(location.getId(), location.getName(), location.getAddress())).toList();
    }

    @Transactional
    public void deleteLocationById(UUID uuid) {
        Location location = locationRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Location not found with id " + uuid)); // TODO: Implement ResourceNotFoundException
        locationRepository.delete(location);
    }

}
