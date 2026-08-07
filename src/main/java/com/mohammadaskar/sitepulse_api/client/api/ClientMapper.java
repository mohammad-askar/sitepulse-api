package com.mohammadaskar.sitepulse_api.client.api;

import com.mohammadaskar.sitepulse_api.client.domain.Client;

public final class ClientMapper {

    private ClientMapper(){

    }

    public static ClientResponse toResponse(Client client){
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getContactEmail(),
                client.getContactPhone(),
                client.getStatus(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }
}
