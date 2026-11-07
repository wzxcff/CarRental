package com.rental.carflow.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLocationRequest(
        @NotBlank(message = "Location name is required")
        @Size(max = 100, message = "Location name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address
) { }
