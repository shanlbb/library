--liquibase formatted sql
--changeset AntonShurayev:20240523-10

CREATE TABLE files (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    original_name VARCHAR(256) NOT NULL,
    type VARCHAR(256) NOT NULL,
    size BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT localtimestamp
)