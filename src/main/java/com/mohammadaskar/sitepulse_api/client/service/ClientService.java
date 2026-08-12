package com.mohammadaskar.sitepulse_api.client.service;

import com.mohammadaskar.sitepulse_api.client.api.ClientMapper;
import com.mohammadaskar.sitepulse_api.client.api.ClientResponse;
import com.mohammadaskar.sitepulse_api.client.api.CreateClientRequest;
import com.mohammadaskar.sitepulse_api.client.api.PageResponse;
import com.mohammadaskar.sitepulse_api.client.domain.Client;
import com.mohammadaskar.sitepulse_api.client.repository.ClientRepository;
import com.mohammadaskar.sitepulse_api.common.exception.DuplicateResourceException;
import com.mohammadaskar.sitepulse_api.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
public class ClientService {

    public final ClientRepository clientRepository;
    private static final int MAX_PAGE_SIZE = 100;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name",
            "status",
            "createdAt"
    );

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

    @Transactional
    public ClientResponse getClient(Long id){
        Client client = clientRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Client with id %d was not found".formatted(id)));

        return ClientMapper.toResponse(client);
    }

    @Transactional(readOnly = true)
    public PageResponse<ClientResponse> getClients(
            int page,
            int size,
            String sortBy,
            String direction
    ){
        validatePagination(page, size, sortBy, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Client> clients = clientRepository.findAll(pageable);

        return new PageResponse<>(
                clients.stream().map(ClientMapper::toResponse).toList(),
                clients.getNumber(),
                clients.getSize(),
                clients.getTotalElements(),
                clients.getTotalPages(),
                clients.isFirst(),
                clients.isLast()
        );
    }

    private void validatePagination(
            int page,
            int size,
            String sortBy,
            String direction
    ){
        if(page < 0) {
            throw new IllegalArgumentException(
                    "Page number must not be negative"
            );
        }
        if(size < 1 || size > MAX_PAGE_SIZE){
            throw new IllegalArgumentException(
                    "Page size must be between 1 and " + MAX_PAGE_SIZE
            );
        }

        if(!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new IllegalArgumentException(
                    "Unsupported sort field: " + sortBy
            );
        }

        if(!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new IllegalArgumentException(
                    "Sort direction must be 'asc' or 'desc'"
            );
        }

    }
}
