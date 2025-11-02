select * from films;

select * from users;

SELECT id FROM likes WHERE film_id = 2;

INSERT INTO likes(film_id, user_id) values (2, 6);
DELETE FROM likes WHERE film_id = 2 AND user_id = 1

select * from MPA_RATING;

SELECT * FROM GENRES;

SELECT * FROM GENRES_IN_FILM AS gif
INNER JOIN GENRES AS g ON g.ID = gif.GENRE_ID where gif.FILM_ID =

SELECT mr.id, mr.name as mpa_rating FROM films as f inner join mpa_rating as mr on mr.ID = f.MPA_RATING_ID;

SELECT mr.id, mr.name as mpa_rating FROM films as f inner join mpa_rating as mr ON mr.ID = f.MPA_RATING_ID
WHERE f.ID = ;

select * from GENRES_IN_FILM;

INSERT INTO films(id, name, description, releaseDate, duration)
VALUES (5, 'Star Wars', 'rethsrt', '2000-01-01', 45);

INSERT INTO GENRES_IN_FILM(film_id, genre_id) values (2, 6);

select top 100 f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_rating_id
from films as f
inner join likes as l
    on l.film_id = f.id
group by f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_rating_id
order by count(l.id) desc