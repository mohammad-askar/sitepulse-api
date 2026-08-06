package com.mohammadaskar.sitepulse_api.client.domain;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByNameIgnoreCase(String name);
}