package com.mohammadaskar.sitepulse_api.client.domain;

import jakarta.persistence.*;
import org.aspectj.apache.bcel.generic.InstructionConstants;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(name = "name", nullable = false, length = 150)
    private String name;

     @Column(name = "contact_email", length = 250)
    private String contactEmail;

     @Column(name = "contact_phone", length = 30)
    private String contactPhone;

     @Enumerated(EnumType.STRING)
     @Column(name = "status", nullable = false, length = 30)
    private ClientStatus status;

     @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

     @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

     public Client(){}

     public Client(String name, String contactEmail, String contactPhone){
         this.name = normalizedRequiredText(name);
         this.contactEmail = normalizedOptionalText(contactEmail);
         this.contactPhone = normalizedOptionalText(contactPhone);

         this.status = ClientStatus.ACTIVE;

         Instant now = Instant.now();

         this.createdAt = now;
         this.updatedAt = now;
     }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private static String normalizedRequiredText(String value){
         return Objects.requireNonNull(value, "value must not be null").trim();
     }

     private static String normalizedOptionalText(String value){
        if(value == null) return null;

        String normalized = value.trim();
        return normalized.isEmpty()? null : normalized;
     }

}
