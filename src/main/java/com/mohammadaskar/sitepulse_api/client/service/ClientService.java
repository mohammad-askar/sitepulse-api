package com.mohammadaskar.sitepulse_api.client.service;

import com.mohammadaskar.sitepulse_api.client.api.ClientMapper;
import com.mohammadaskar.sitepulse_api.client.api.ClientResponse;
import com.mohammadaskar.sitepulse_api.client.api.CreateClientRequest;
import com.mohammadaskar.sitepulse_api.client.domain.Client;
import com.mohammadaskar.sitepulse_api.client.repository.ClientRepository;
import com.mohammadaskar.sitepulse_api.common.exception.DuplicateResourceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    public final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientResponse createClient(CreateClientRequest request){
        String normalizedName = request.name().trim();

        if(clientRepository.existsByNameIgnoreCase(normalizedName)){
            throw new DuplicateResourceException("A client with the same name already exists");
        }

        Client client = new Client(normalizedName, request.contactEmail(), request.contactPhone());

        Client savedClient = clientRepository.save(client);

        return ClientMapper.toResponse(savedClient);
    }
}
