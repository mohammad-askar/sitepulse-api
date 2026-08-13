package com.mohammadaskar.sitepulse_api.client.service;

import com.mohammadaskar.sitepulse_api.client.domain.Client;
import com.mohammadaskar.sitepulse_api.client.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldSaveAndFindClient() {
        Client client = new Client(
                "Nova Medical Center",
                "contact@nova.example",
                "+49 151 12345678"
        );

        Client savedClient = clientRepository.saveAndFlush(client);

        assertThat(savedClient.getId()).isNotNull();

        assertThat(
                clientRepository.findById(savedClient.getId())
        ).isPresent();
    }

    @Test
    void shouldRejectDuplicateClientNamesIgnoringCase() {
        Client firstClient = new Client(
                "Nova Medical Center",
                null,
                null
        );

        clientRepository.saveAndFlush(firstClient);

        Client duplicateClient = new Client(
                "NOVA MEDICAL CENTER",
                null,
                null
        );

        assertThatThrownBy(() ->
                clientRepository.saveAndFlush(duplicateClient)
        )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDetectExistingClientIgnoringCase() {
        Client client = new Client(
                "Nova Medical Center",
                null,
                null
        );

        clientRepository.saveAndFlush(client);

        boolean exists =
                clientRepository.existsByNameIgnoreCase(
                        "NOVA MEDICAL CENTER"
                );

        assertThat(exists).isTrue();
    }
}