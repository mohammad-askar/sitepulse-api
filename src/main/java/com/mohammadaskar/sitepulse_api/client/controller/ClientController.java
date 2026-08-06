package com.mohammadaskar.sitepulse_api.client.controller;

import com.mohammadaskar.sitepulse_api.client.api.ClientResponse;
import com.mohammadaskar.sitepulse_api.client.api.CreateClientRequest;
import com.mohammadaskar.sitepulse_api.client.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
