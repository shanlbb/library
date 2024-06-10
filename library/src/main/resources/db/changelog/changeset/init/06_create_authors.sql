--liquibase formatted sql
--changeset AntonShurayev:20240523-06

CREATE TABLE authors (
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     first_name VARCHAR(255) NOT NULL,
     last_name VARCHAR(255) NOT NULL,
     CONSTRAINT unique_name UNIQUE (first_name, last_name)
);