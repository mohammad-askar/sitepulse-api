package com.mohammadaskar.sitepulse_api.client.controller;

import com.mohammadaskar.sitepulse_api.client.api.ClientResponse;
import com.mohammadaskar.sitepulse_api.client.api.CreateClientRequest;
import com.mohammadaskar.sitepulse_api.client.api.PageResponse;
import com.mohammadaskar.sitepulse_api.client.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    public final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request){
        ClientResponse clientResponse = clientService.createClient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(clientResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(
            @PathVariable Long id){
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ClientResponse>> getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return ResponseEntity.ok(clientService.getClients(page, size, sortBy, direction));
    }
}
