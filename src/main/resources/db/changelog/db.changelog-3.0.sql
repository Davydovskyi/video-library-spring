--liquibase formatted sql

--changeset Davydovskyi:1.0
ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE users
    ADD COLUMN updated_at TIMESTAMP;

ALTER TABLE users
    ADD COLUMN created_by VARCHAR(124);

ALTER TABLE users
    ADD COLUMN updated_by VARCHAR(124);

--changeset Davydovskyi:2.0
ALTER TABLE movie
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE movie
    ADD COLUMN updated_at TIMESTAMP;

ALTER TABLE movie
    ADD COLUMN created_by VARCHAR(124);

ALTER TABLE movie
    ADD COLUMN updated_by VARCHAR(124);