package com.rental.carflow.location;

import com.rental.carflow.common.ConflictException;
import com.rental.carflow.common.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    // Arrange-Act-Assert

    @Mock
    private LocationRepository repository;

    @InjectMocks
    private LocationService service;

    @Test
    void getAll_whenEmpty_returnEmptyList() {
        when(repository.findAll()).thenReturn(List.of());

        var result = service.getAllLocations();

        assertTrue(result.isEmpty());
    }

    @Test
    void createLocation_whenNameIsNew_savesAndReturnsLocation() {
        CreateLocationRequest request = new CreateLocationRequest("Airport", "Terminal 1");
        UUID expectedId = UUID.randomUUID();
        Location savedLocation = new Location(expectedId, "Airport", "Terminal 1");

        when(repository.existsByName("Airport")).thenReturn(false);
        when(repository.save(any(Location.class))).thenReturn(savedLocation);
        LocationDTO result = service.createLocation(request);

        assertNotNull(result);
        assertEquals(expectedId, result.id());
        assertEquals("Airport", result.name());
        assertEquals("Terminal 1", result.address());
        verify(repository).save(any(Location.class));
    }

    @Test
    void createLocation_whenNameExists_throwsConflictException() {
        CreateLocationRequest request = new CreateLocationRequest("Airport", "Terminal 1");

        when(repository.existsByName("Airport")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.createLocation(request));
        verify(repository, never()).save(any(Location.class));
    }

    @Test
    void getById_whenExists_returnsLocation() {
        UUID expectedId = UUID.randomUUID();
        Location savedLocation = new Location(expectedId, "Airport", "Terminal 1");

        when(repository.findById(expectedId)).thenReturn(Optional.of(savedLocation));
        LocationDTO result = service.getLocationById(expectedId);

        assertNotNull(result);
        assertEquals(expectedId, result.id());
        assertEquals("Airport", result.name());
        assertEquals("Terminal 1", result.address());
    }

    @Test
    void getById_whenNotExists_throwsNotFoundException() {
        UUID expectedId = UUID.randomUUID();

        when(repository.findById(expectedId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getLocationById(expectedId));
    }

    @Test
    void getAll_whenPopulated_returnsLocations() {
        UUID expectedId = UUID.randomUUID();
        Location savedLocation = new Location(expectedId, "Airport", "Terminal 1");

        when(repository.findAll()).thenReturn(List.of(savedLocation, savedLocation));
        List<LocationDTO> result = service.getAllLocations();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void delete_whenExists_deletesById() {
        UUID expectedId = UUID.randomUUID();
        Location savedLocation = new Location(expectedId, "Airport", "Terminal 1");

        when(repository.findById(expectedId)).thenReturn(Optional.of(savedLocation));
        service.deleteLocationById(expectedId);

        verify(repository).delete(savedLocation);
    }

    @Test
    void delete_whenNotExists_throwsNotFoundException() {
        UUID expectedId = UUID.randomUUID();

        when(repository.findById(expectedId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteLocationById(expectedId));
        verify(repository, never()).delete(any(Location.class));
    }
}
