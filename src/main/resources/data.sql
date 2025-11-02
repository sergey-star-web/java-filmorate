INSERT INTO GENRES (name, const) SELECT 'Комедия', 'COMEDY'
WHERE NOT EXISTS (SELECT 1 FROM GENRES WHERE name = 'Комедия');

INSERT INTO GENRES (name, const) SELECT 'Драма', 'DRAMA'
WHERE NOT EXISTS (SELECT 1 FROM GENRES WHERE name = 'Драма');

INSERT INTO GENRES (name, const) SELECT 'Мультфильм', 'CARTOON'
WHERE NOT EXISTS (SELECT 1 FROM GENRES WHERE name = 'Мультфильм');

INSERT INTO GENRES (name, const) SELECT 'Триллер', 'THRILLER'
WHERE NOT EXISTS (SELECT 1 FROM GENRES WHERE name = 'Триллер');

INSERT INTO GENRES (name, const) SELECT 'Документальный', 'DOCUMENTARY'
WHERE NOT EXISTS (SELECT 1 FROM GENRES WHERE name = 'Документальный');

INSERT INTO GENRES (name, const) SELECT 'Боевик', 'ACTION'
WHERE NOT EXISTS (SELECT 1 FROM GENRES WHERE name = 'Боевик');

INSERT INTO MPA_RATING (name, description) SELECT 'G', 'у фильма нет возрастных ограничений'
WHERE NOT EXISTS (SELECT 1 FROM MPA_RATING WHERE name = 'G');

INSERT INTO MPA_RATING (name, description) SELECT 'PG', 'детям рекомендуется смотреть фильм с родителями'
WHERE NOT EXISTS (SELECT 1 FROM MPA_RATING WHERE name = 'PG');

INSERT INTO MPA_RATING (name, description) SELECT 'PG-13', 'детям до 13 лет просмотр не желателен'
WHERE NOT EXISTS (SELECT 1 FROM MPA_RATING WHERE name = 'PG-13');

INSERT INTO MPA_RATING (name, description) SELECT 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
WHERE NOT EXISTS (SELECT 1 FROM MPA_RATING WHERE name = 'R');

INSERT INTO MPA_RATING (name, description) SELECT 'NC-17', 'лицам до 18 лет просмотр запрещён'
WHERE NOT EXISTS (SELECT 1 FROM MPA_RATING WHERE name = 'NC-17');

INSERT INTO films (name, description, releaseDate, duration, mpa_rating_id)
SELECT 'Harry Potter', 'asgsdgsg', '2010-02-15', 90, 3
WHERE NOT EXISTS (SELECT 1 FROM films WHERE name = 'Harry Potter' and releaseDate = '2010-02-15');

INSERT INTO films (name, description, releaseDate, duration, mpa_rating_id)
SELECT 'Star Wars 3', 'rtjhrwsj', '2000-01-14 12:30:00', 110, 2
WHERE NOT EXISTS (SELECT 1 FROM films WHERE name = 'Star Wars 3' and releaseDate = '2000-01-14 12:30:00');

INSERT INTO films (name, description, releaseDate, duration, mpa_rating_id)
SELECT 'The War Of The Worlds', 'rthjrt345t34', '2025-08-11 14:00:00', 89, 1
WHERE NOT EXISTS (SELECT 1 FROM films WHERE name = 'The War Of The Worlds' and releaseDate = '2025-08-11 14:00:00');

INSERT INTO genres_in_film (film_id, genre_id)
SELECT 1, 2
WHERE NOT EXISTS (SELECT 1 FROM genres_in_film WHERE film_id = 1 and genre_id = 2);

INSERT INTO genres_in_film (film_id, genre_id)
SELECT CAST(2 AS BIGINT), CAST(2 AS BIGINT)
WHERE NOT EXISTS (SELECT 1 FROM genres_in_film WHERE film_id = CAST(2 AS BIGINT) and genre_id = CAST(2 AS BIGINT));

INSERT INTO genres_in_film (film_id, genre_id)
SELECT CAST(2 AS BIGINT), CAST(6 AS BIGINT)
WHERE NOT EXISTS (SELECT 1 FROM genres_in_film WHERE film_id = CAST(2 AS BIGINT) and genre_id = CAST(6 AS BIGINT));

INSERT INTO genres_in_film (film_id, genre_id)
SELECT CAST(3 AS BIGINT), CAST(6 AS BIGINT)
WHERE NOT EXISTS (SELECT 1 FROM genres_in_film WHERE film_id = CAST(3 AS BIGINT) and genre_id = CAST(6 AS BIGINT));

INSERT INTO genres_in_film (film_id, genre_id)
SELECT CAST(3 AS BIGINT), CAST(5 AS BIGINT)
WHERE NOT EXISTS (SELECT 1 FROM genres_in_film WHERE film_id = CAST(3 AS BIGINT) and genre_id = CAST(5 AS BIGINT));

INSERT INTO users (email, login, name, birthday)
SELECT 'dragon777@gmail.com', 'dragon777', null, '2001-08-31'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'dragon777@gmail.com');

INSERT INTO users (email, login, name, birthday)
SELECT 'seconduser@mail.ru', 'seconduser121', 'seconduser', '2005-04-20'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'seconduser@mail.ru');

INSERT INTO users (email, login, name, birthday)
SELECT 'testuser@mail.ru', 'testuser999', 'testuser', '2009-03-11 '
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'testuser@mail.ru');

INSERT INTO friends (user_send_id, user_received_id, confirm_friendship_status)
SELECT 1, 2, false
WHERE NOT EXISTS (SELECT 1 FROM friends WHERE user_send_id = 1 and user_received_id = 2);

INSERT INTO friends (user_send_id, user_received_id, confirm_friendship_status)
SELECT 1, 3, false
WHERE NOT EXISTS (SELECT 1 FROM friends WHERE user_send_id = 1 and user_received_id = 3);

INSERT INTO friends (user_send_id, user_received_id, confirm_friendship_status)
SELECT 2, 3, true
WHERE NOT EXISTS (SELECT 1 FROM friends WHERE user_send_id = 2 and user_received_id = 3);

INSERT INTO likes (user_id, film_id)
SELECT 1, 2
WHERE NOT EXISTS (SELECT 1 FROM likes WHERE user_id = 1 and film_id = 2);

INSERT INTO likes (user_id, film_id)
SELECT 1, 3
WHERE NOT EXISTS (SELECT 1 FROM likes WHERE user_id = 1 and film_id = 3);

INSERT INTO likes (user_id, film_id)
SELECT 1, 2
WHERE NOT EXISTS (SELECT 1 FROM likes WHERE user_id = 1 and film_id = 2);

INSERT INTO likes (user_id, film_id)
SELECT 2, 2
WHERE NOT EXISTS (SELECT 1 FROM likes WHERE user_id = 2 and film_id = 2);

INSERT INTO likes (user_id, film_id)
SELECT 2, 3
WHERE NOT EXISTS (SELECT 1 FROM likes WHERE user_id = 2 and film_id = 3);

INSERT INTO likes (user_id, film_id)
SELECT 3, 1
WHERE NOT EXISTS (SELECT 1 FROM likes WHERE user_id = 3 and film_id = 1);
