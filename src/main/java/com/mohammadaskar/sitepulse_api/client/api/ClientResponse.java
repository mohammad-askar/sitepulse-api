package com.mohammadaskar.sitepulse_api.client.api;

import com.mohammadaskar.sitepulse_api.client.domain.ClientStatus;

import java.time.Instant;

public record ClientResponse(
        Long id,
        String name,
        String contactEmail,
        String contactPhone,
        ClientStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
