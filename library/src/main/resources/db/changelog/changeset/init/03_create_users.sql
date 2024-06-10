--liquibase formatted sql
--changeset AntonShurayev:20240523-03

CREATE TABLE users (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email VARCHAR(256) NOT NULL UNIQUE,
    username VARCHAR(16) NOT NULL,
    password_hash VARCHAR(256) NOT NULL,
    role_id INTEGER NOT NULL REFERENCES roles(id)
)