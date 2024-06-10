--liquibase formatted sql
--changeset AntonShurayev:20240523-05

CREATE TABLE refresh_tokens (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    expires_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE
)