package com.rental.carflow.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class LocationControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.2-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository repository;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    void getAll_whenEmpty_returnsEmptyArray() throws Exception {
        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createLocation_whenValid_returns201() throws Exception {
        String body = """
            {"name": "Airport", "address": "Terminal 1"}
            """;

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Airport"))
                .andExpect(jsonPath("$.address").value("Terminal 1"));
    }

    @Test
    void getById_whenExists_returnsLocation() throws Exception {
        Location saved = repository.save(new Location(null, "Airport", "Terminal 1"));

        mockMvc.perform(get("/api/v1/locations/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                .andExpect(jsonPath("$.name").value("Airport"))
                .andExpect(jsonPath("$.address").value("Terminal 1"));
    }

    @Test
    void getById_whenNotExists_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/locations/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"));
    }

    @Test
    void getAll_whenPopulated_returnsLocations() throws Exception {
        repository.saveAll(List.of(
                new Location(null, "Airport 0", "Terminal 0"),
                new Location(null, "Airport 1", "Terminal 1"),
                new Location(null, "Airport 2", "Terminal 2")
        ));

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void deleteLocation_whenExists_returns204AndDeletesFromDb() throws Exception {
        Location saved = repository.save(new Location(null, "Airport", "Terminal 1"));
        UUID id = saved.getId();

        mockMvc.perform(delete("/api/v1/locations/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/locations/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLocation_whenNotExist_returns404() throws Exception {
        mockMvc.perform(delete("/api/v1/locations/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"));
    }

    @Test
    void createLocation_whenAlreadyExists_returns409() throws Exception {
        repository.save(new Location(null, "Airport", "Terminal 1"));
        String body = """
            {"name": "Airport", "address": "Terminal 1"}
            """;

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict occurred"));
    }

    @Test
    void getById_whenIdNotValid_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/locations/abc"))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"name\": \"\", \"address\": \"Terminal 1\"}",
            "{\"name\": \"Airport\"}",
            "{\"name\": \"Airport\", \"address\": null}"
    })
    void createLocation_whenInvalid_returns400(String body) throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLocation_whenNameTooLong_returns400() throws Exception {
        String body = """
            {"name": "%s", "address": "Terminal 1"}
            """.formatted("a".repeat(101));

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
