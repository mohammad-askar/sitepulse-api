package com.mohammadaskar.sitepulse_api.client.service;

import com.mohammadaskar.sitepulse_api.client.api.ClientResponse;
import com.mohammadaskar.sitepulse_api.client.api.CreateClientRequest;
import com.mohammadaskar.sitepulse_api.client.domain.Client;
import com.mohammadaskar.sitepulse_api.client.domain.ClientStatus;
import com.mohammadaskar.sitepulse_api.client.repository.ClientRepository;
import com.mohammadaskar.sitepulse_api.common.exception.DuplicateResourceException;
import com.mohammadaskar.sitepulse_api.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClientWhenNameDoesNotExist() {
        CreateClientRequest request = new CreateClientRequest(
                "  Nova Medical Center  ",
                " contact@nova.example ",
                " +49 151 12345678 "
        );

        when(clientRepository.existsByNameIgnoreCase("Nova Medical Center"))
                .thenReturn(false);

        when(clientRepository.save(org.mockito.ArgumentMatchers.any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ClientResponse response = clientService.createClient(request);

        assertThat(response.name()).isEqualTo("Nova Medical Center");
        assertThat(response.contactEmail()).isEqualTo("contact@nova.example");
        assertThat(response.contactPhone()).isEqualTo("+49 151 12345678");
        assertThat(response.status()).isEqualTo(ClientStatus.ACTIVE);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();

        ArgumentCaptor<Client> clientCaptor =
                ArgumentCaptor.forClass(Client.class);

        verify(clientRepository).save(clientCaptor.capture());

        Client savedClient = clientCaptor.getValue();

        assertThat(savedClient.getName()).isEqualTo("Nova Medical Center");
        assertThat(savedClient.getStatus()).isEqualTo(ClientStatus.ACTIVE);
    }

    @Test
    void shouldRejectClientWhenNameAlreadyExists() {
        CreateClientRequest request = new CreateClientRequest(
                "Nova Medical Center",
                "contact@nova.example",
                null
        );

        when(clientRepository.existsByNameIgnoreCase("Nova Medical Center"))
                .thenReturn(true);

        assertThatThrownBy(() -> clientService.createClient(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("A client with the same name already exists");

        verify(clientRepository, never())
                .save(org.mockito.ArgumentMatchers.any(Client.class));
    }

    @Test
    void shouldReturnClientWhenClientExists() {
        Client client = new Client(
                "Nova Medical Center",
                "contact@nova.example",
                null
        );

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        ClientResponse response = clientService.getClient(1L);

        assertThat(response.name())
                .isEqualTo("Nova Medical Center");

        verify(clientRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenClientDoesNotExist() {
        when(clientRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClient(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Client with id 999 was not found");
    }
}
