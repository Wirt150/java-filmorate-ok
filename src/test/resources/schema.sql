DROP TABLE IF EXISTS FILMS_LIKE CASCADE;
DROP TABLE IF EXISTS FRIENDS CASCADE;
DROP TABLE IF EXISTS GENRES_FILM CASCADE;
DROP TABLE IF EXISTS FILMS CASCADE;
DROP TABLE IF EXISTS GENRES CASCADE;
DROP TABLE IF EXISTS MPA CASCADE;
DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS REVIEW CASCADE;
DROP TABLE IF EXISTS REVIEW_LIKES CASCADE;
DROP TABLE IF EXISTS REVIEW_DISLIKES CASCADE;
DROP TABLE IF EXISTS EVENT CASCADE;
DROP TABLE IF EXISTS DIRECTORS CASCADE;
DROP TABLE IF EXISTS DIRECTORS_FILM CASCADE;

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   INTEGER GENERATED BY DEFAULT AS IDENTITY,
    mpa_name VARCHAR UNIQUE,
    CONSTRAINT mpa_pk PRIMARY KEY (mpa_id)
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id   INTEGER GENERATED BY DEFAULT AS IDENTITY,
    genre_name VARCHAR UNIQUE,
    CONSTRAINT genres_pk PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS directors
(
    director_id   INTEGER GENERATED BY DEFAULT AS IDENTITY,
    director_name VARCHAR,
    CONSTRAINT directors_pk PRIMARY KEY (director_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INTEGER GENERATED BY DEFAULT AS IDENTITY,
    email    VARCHAR NOT NULL UNIQUE,
    login    VARCHAR NOT NULL,
    name     VARCHAR,
    birthday DATE,
    CONSTRAINT user_pk PRIMARY KEY (user_id),
    CONSTRAINT check_birthday CHECK (birthday <= CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS films
(
    film_id      INTEGER GENERATED BY DEFAULT AS IDENTITY,
    name         VARCHAR NOT NULL,
    description  VARCHAR(200),
    release_date DATE,
    duration     INTEGER,
    rate         INTEGER,
    mpa_id       INTEGER REFERENCES mpa (mpa_id),
    CONSTRAINT film_pk PRIMARY KEY (film_id),
    CONSTRAINT check_release_date CHECK (release_date >= '1895-12-28'),
    CONSTRAINT check_duration CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS review
(
    review_id   INTEGER GENERATED BY DEFAULT AS IDENTITY,
    content     VARCHAR,
    is_positive BOOLEAN,
    user_id     INTEGER REFERENCES users (user_id),
    film_id     INTEGER REFERENCES films (film_id),
    useful      INTEGER,
    CONSTRAINT review_pk PRIMARY KEY (review_id)
);

CREATE TABLE IF NOT EXISTS event
(
    event_id INTEGER GENERATED BY DEFAULT AS IDENTITY,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE ,
    entity_id INTEGER,
    timestamp_event TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP(),
    event_type VARCHAR (6),
    operation VARCHAR (6),
    CONSTRAINT event_pk PRIMARY KEY (event_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id   INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT friends_pk PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS genres_film
(
    film_id  INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE,
    CONSTRAINT genres_film_pk PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS directors_film
(
    film_id     INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    director_id INTEGER REFERENCES directors (director_id) ON DELETE CASCADE,
    CONSTRAINT directors_film_pk PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS films_like
(
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT films_like_pk PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS review_likes
(
    review_id INTEGER REFERENCES review (review_id) ON DELETE CASCADE,
    user_id   INTEGER REFERENCES users (user_id) ON DELETE CASCADE,

    CONSTRAINT review_likes_pk PRIMARY KEY (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS review_dislikes
(
    review_id INTEGER REFERENCES review (review_id) ON DELETE CASCADE,
    user_id   INTEGER REFERENCES users (user_id) ON DELETE CASCADE,

    CONSTRAINT review_dislikes_pk PRIMARY KEY (review_id, user_id)
);


INSERT INTO MPA(MPA_NAME)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');
INSERT INTO GENRES(GENRE_NAME)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');
