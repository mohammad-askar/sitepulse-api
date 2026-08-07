package com.mohammadaskar.sitepulse_api.client.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientRequest(

        @NotBlank(message = "Client name is required")
        @Size(max = 150, message = "Client name must not exceed 150 characters")
        String name,

        @Email(message = "Contact email must be valid")
        @Size(max = 254, message = "Contact email must not exceed 254 characters")
        String contactEmail,

        @Size(max = 30, message = "Contact phone must not exceed 30 characters")
        String contactPhone
) {


}
