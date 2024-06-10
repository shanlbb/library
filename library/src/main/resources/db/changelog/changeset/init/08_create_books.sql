--liquibase formatted sql
--changeset AntonShurayev:20240523-08

CREATE TABLE books (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    pages INTEGER NOT NULL,
    publish_date DATE,
    downloads BIGINT NOT NULL DEFAULT 0,
    rating DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    user_id UUID NOT NULL REFERENCES users(id)
);