--liquibase formatted sql
--changeset AntonShurayev:20240523-11

CREATE TABLE book_cover_files (
    id UUID PRIMARY KEY REFERENCES files(id) ON DELETE CASCADE,
    book_id UUID NOT NULL UNIQUE REFERENCES books(id)
);