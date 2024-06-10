--liquibase formatted sql
--changeset AntonShurayev:20240523-09

CREATE TABLE reviews (
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     author VARCHAR(255) NOT NULL,
     rating INTEGER NOT NULL,
     review TEXT,
     created_at TIMESTAMP NOT NULL,
     book_id UUID NOT NULL REFERENCES books(id) ON DELETE CASCADE
);