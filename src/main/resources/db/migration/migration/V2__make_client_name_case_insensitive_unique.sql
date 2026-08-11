ALTER TABLE clients
    DROP CONSTRAINT uk_clients_name;

CREATE UNIQUE INDEX uk_clients_name_ci
    ON clients (LOWER(name));