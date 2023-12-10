INSERT INTO person (person_id, person_name, person_birth_date)
VALUES (1, 'Emma Watson', '1990-04-25'),
       (2, 'Brad Pitt', '1963-12-18'),
       (3, 'Danial Radcliffe', '1989-07-23');
SELECT setval('person_person_id_seq', (SELECT MAX(person_id) FROM person), true);

INSERT INTO movie (movie_id, movie_title, release_year, movie_country, movie_genre, movie_description)
VALUES (1, 'The Dark Knight', 2008, 'the USA', 'ACTION',
        'The plot follows the vigilante Batman, police lieutenant James Gordon, and district attorney Harvey Dent, who form an alliance to dismantle organized crime in Gotham City. Their efforts are derailed by the Joker, an anarchistic mastermind who seeks to test how far Batman will go to save the city from chaos.'),
       (2, 'Inception', 2010, 'the USA', 'SCIENCE_FICTION',
        'Summaries. A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.'),
       (3, 'Tenet', 2020, 'United Kingdom', 'SCIENCE_FICTION',
        'The film follows a former CIA agent who learns how to manipulate the flow of time to prevent an attack from the future that threatens to annihilate the present world.');
SELECT setval('movie_movie_id_seq', (SELECT MAX(movie_id) FROM movie), true);

INSERT INTO movie_person (id, movie_id, person_id, person_role)
VALUES (1, (SELECT movie_id FROM movie WHERE movie_title = 'The Dark Knight'),
        (SELECT person_id FROM person WHERE person_name = 'Emma Watson'), 'COMPOSER'),
       (2, (SELECT movie_id FROM movie WHERE movie_title = 'The Dark Knight'),
        (SELECT person_id FROM person WHERE person_name = 'Emma Watson'), 'ACTOR'),
       (3, (SELECT movie_id FROM movie WHERE movie_title = 'Inception'),
        (SELECT person_id FROM person WHERE person_name = 'Brad Pitt'), 'DIRECTOR'),
       (4, (SELECT movie_id FROM movie WHERE movie_title = 'Inception'),
        (SELECT person_id FROM person WHERE person_name = 'Brad Pitt'), 'ACTOR'),
       (5, (SELECT movie_id FROM movie WHERE movie_title = 'Tenet'),
        (SELECT person_id FROM person WHERE person_name = 'Danial Radcliffe'), 'ACTOR');
SELECT setval('movie_person_id_seq', (SELECT MAX(id) FROM movie_person), true);

INSERT INTO users (user_id, user_name, user_birth_date, user_image, user_email, user_password, role, gender)
VALUES (1, 'user1', '2000-01-01', null, 'user1@gmail.com', '{noop}123', 'USER', 'MALE'),
       (2, 'user2', '1999-01-01', null, 'user2@gmail.com', '{noop}112', 'USER', 'FEMALE'),
       (3, 'admin', '1990-01-01', 'testImage.PNG', 'admin@gmail.com', '{noop}998', 'ADMIN', 'MALE');
SELECT setval('users_user_id_seq', (SELECT MAX(user_id) FROM users), true);

INSERT INTO review (review_id, movie_id, user_id, review_text, rate)
VALUES (1, (SELECT movie_id FROM movie WHERE movie_title = 'The Dark Knight'),
        (SELECT user_id FROM users WHERE user_name = 'user1'), 'Nice movie.', 9),
       (2, (SELECT movie_id FROM movie WHERE movie_title = 'Inception'),
        (SELECT user_id FROM users WHERE user_name = 'user2'),
        'One of my favorite movies. Watched it many times, definitely recommended!', 10),
       (3, (SELECT movie_id FROM movie WHERE movie_title = 'Tenet'),
        (SELECT user_id FROM users WHERE user_name = 'admin'), 'Such a lovely story, absolutely love it!', 10),
       (4, (SELECT movie_id FROM movie WHERE movie_title = 'Inception'),
        (SELECT user_id FROM users WHERE user_name = 'user1'), 'Nolan is so genius', 7),
       (5, (SELECT movie_id FROM movie WHERE movie_title = 'Tenet'),
        (SELECT user_id FROM users WHERE user_name = 'user2'), 'Good one', 8);
SELECT setval('review_review_id_seq', (SELECT MAX(review_id) FROM review), true);