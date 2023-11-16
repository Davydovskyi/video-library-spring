--liquibase formatted sql

--changeset Davydovskyi:1.0
DROP TABLE IF EXISTS movie_aud;
DROP TABLE IF EXISTS person_aud;
DROP TABLE IF EXISTS users_aud;
DROP TABLE IF EXISTS revision;

--changeset Davydovskyi:2.0
CREATE TABLE revision
(
    id        SERIAL NOT NULL,
    timestamp BIGINT,
    PRIMARY KEY (id)
);

--changeset Davydovskyi:3.0
CREATE TABLE movie_aud
(
    movie_id          INTEGER NOT NULL,
    rev               INTEGER NOT NULL REFERENCES revision (id),
    revtype           SMALLINT,
    movie_country     VARCHAR(255),
    movie_description VARCHAR(255),
    movie_genre       VARCHAR(255),
    release_year      SMALLINT,
    movie_title       VARCHAR(255),
    PRIMARY KEY (rev, movie_id)
);

--changeset Davydovskyi:4.0
CREATE TABLE person_aud
(
    person_id         INTEGER NOT NULL,
    rev               INTEGER NOT NULL REFERENCES revision (id),
    revtype           SMALLINT,
    person_birth_date DATE,
    person_name       VARCHAR(255),
    PRIMARY KEY (rev, person_id)
);

--changeset Davydovskyi:5.0
CREATE TABLE users_aud
(
    user_id         BIGINT  NOT NULL,
    rev             INTEGER NOT NULL REFERENCES revision (id),
    revtype         SMALLINT,
    user_birth_date DATE,
    user_email      VARCHAR(255),
    user_image      VARCHAR(255),
    gender          VARCHAR(255),
    user_name       VARCHAR(255),
    user_password   VARCHAR(255),
    role            VARCHAR(255),
    PRIMARY KEY (rev, user_id)
);