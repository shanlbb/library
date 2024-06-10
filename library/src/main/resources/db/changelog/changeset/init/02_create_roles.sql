--liquibase formatted sql
--changeset AntonShurayev:20240523-02


CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL UNIQUE
)