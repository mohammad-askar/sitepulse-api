package com.mohammadaskar.sitepulse_api.client.repository;


import com.mohammadaskar.sitepulse_api.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByNameIgnoreCase(String name);
}