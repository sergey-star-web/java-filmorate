select * from films;

SELECT id FROM likes WHERE film_id = 2;

select * from MPA_RATING;

SELECT * FROM GENRES;

SELECT * FROM GENRES_IN_FILM AS gif
INNER JOIN GENRES AS g ON g.ID = gif.GENRE_ID where gif.FILM_ID =

SELECT f.*, mr.name as mpa_rating FROM films as f inner join mpa_rating as mr on mr.ID = f.MPA_RATING_ID;

SELECT f.*, mr.name as mpa_rating FROM films as f inner join mpa_rating as mr ON mr.ID = f.MPA_RATING_ID;

select * from GENRES_IN_FILM;

INSERT INTO films(id, name, description, releaseDate, duration)
VALUES (5, 'Star Wars', 'rethsrt', '2000-01-01', 45);
select @@identifier
