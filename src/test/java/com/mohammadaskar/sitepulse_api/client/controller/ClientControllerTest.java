package com.mohammadaskar.sitepulse_api.client.controller;

import com.mohammadaskar.sitepulse_api.client.api.ClientResponse;
import com.mohammadaskar.sitepulse_api.client.api.CreateClientRequest;
import com.mohammadaskar.sitepulse_api.client.domain.ClientStatus;
import com.mohammadaskar.sitepulse_api.client.service.ClientService;
import com.mohammadaskar.sitepulse_api.common.exception.DuplicateResourceException;
import com.mohammadaskar.sitepulse_api.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@Import(GlobalExceptionHandler.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    @Test
    void shouldReturn201WhenClientIsCreated() throws Exception {
        CreateClientRequest request = new CreateClientRequest(
                "Nova Medical Center",
                "contact@nova.example",
                "+49 151 12345678"
        );

        Instant createdAt = Instant.parse("2026-08-07T00:00:00Z");

        ClientResponse response = new ClientResponse(
                1L,
                "Nova Medical Center",
                "contact@nova.example",
                "+49 151 12345678",
                ClientStatus.ACTIVE,
                createdAt,
                createdAt
        );

        when(clientService.createClient(any(CreateClientRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Nova Medical Center"))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$.contactEmail")
                        .value("contact@nova.example"));
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        String invalidRequest = """
                {
                  "name": "   ",
                  "contactEmail": "not-an-email",
                  "contactPhone": "123456789012345678901234567890123"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Request validation failed"))
                .andExpect(jsonPath("$.validationErrors.name")
                        .value("Client name is required"))
                .andExpect(jsonPath("$.validationErrors.contactEmail")
                        .value("Contact email must be valid"))
                .andExpect(jsonPath("$.validationErrors.contactPhone")
                        .value("Contact phone must not exceed 30 characters"));
    }

    @Test
    void shouldReturn409WhenClientNameAlreadyExists() throws Exception {
        CreateClientRequest request = new CreateClientRequest(
                "Nova Medical Center",
                "contact@nova.example",
                null
        );

        when(clientService.createClient(any(CreateClientRequest.class)))
                .thenThrow(new DuplicateResourceException(
                        "A client with the same name already exists"
                ));

        mockMvc.perform(
                        post("/api/v1/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("A client with the same name already exists"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/clients"));
    }
}