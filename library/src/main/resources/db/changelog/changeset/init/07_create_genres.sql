--liquibase formatted sql
--changeset AntonShurayev:20240523-07

CREATE TABLE genres (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);