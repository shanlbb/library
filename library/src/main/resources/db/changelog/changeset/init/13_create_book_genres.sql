--liquibase formatted sql
--changeset AntonShurayev:20240523-13

CREATE TABLE book_genres (
     book_id UUID NOT NULL REFERENCES books(id) ON DELETE CASCADE,
     genre_id UUID NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
     PRIMARY KEY (book_id, genre_id)
);