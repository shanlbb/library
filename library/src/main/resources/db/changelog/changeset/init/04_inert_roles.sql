--liquibase formatted sql
--changeset AntonShurayev:20240523-04

INSERT INTO roles (name)
VALUES ('ROLE_PUBLISHER')