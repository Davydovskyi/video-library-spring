DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS movie_person;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    user_id         BIGSERIAL PRIMARY KEY,
    user_name       VARCHAR(124) NOT NULL,
    user_birth_date DATE,
    user_image      VARCHAR(124),
    user_email      VARCHAR(124) NOT NULL UNIQUE,
    user_password   VARCHAR(32)  NOT NULL,
    role            VARCHAR(32)  NOT NULL,
    gender          VARCHAR(16)
);

CREATE TABLE person
(
    person_id         SERIAL PRIMARY KEY,
    person_name       VARCHAR(256) NOT NULL,
    person_birth_date DATE         NOT NULL,
    UNIQUE (person_name, person_birth_date)
);

CREATE TABLE movie
(
    movie_id          SERIAL PRIMARY KEY,
    movie_title       VARCHAR(256) NOT NULL,
    release_year      SMALLINT     NOT NULL,
    movie_country     VARCHAR(124) NOT NULL,
    movie_genre       VARCHAR(124) NOT NULL,
    movie_description TEXT         NOT NULL
);

CREATE INDEX movie_title_idx ON movie (movie_title);
CREATE INDEX movie_release_year_idx ON movie (release_year);

CREATE TABLE movie_person
(
    id          BIGSERIAL PRIMARY KEY,
    movie_id    INTEGER      NOT NULL REFERENCES movie (movie_id) ON DELETE RESTRICT,
    person_id   INTEGER      NOT NULL REFERENCES person (person_id) ON DELETE RESTRICT,
    person_role VARCHAR(124) NOT NULL
);

CREATE TABLE review
(
    review_id   SERIAL PRIMARY KEY,
    movie_id    INTEGER      NOT NULL REFERENCES movie (movie_id) ON DELETE CASCADE,
    user_id     INTEGER      NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    review_text VARCHAR(256) NOT NULL,
    rate        SMALLINT     NOT NULL,
    UNIQUE (movie_id, user_id)
);