# java-filmorate
## Диаграмма базы данных
- Таблицы:
    - `films` — фильмы
    - `genres` — жанры фильмов
    - `genres_in_film` — промежуточная таблица между `films` и `genres`
    - `mpa_rating` — возрастные рейтинги фильмов
    - `user` — пользователи
    - `likes` — лайки
    - `friends` — друзья

```sql
select * from films -- все фильмы
select * from user --  всех пользователи
select * from films where id = ? -- конкретный фильм
-- фильмы с жанрами отсортированные по жанру
select g.id, g.name, f.name
from genres_in_film as gif
inner join genres as g 
    on g.ID = gif.genre_id
inner join films as f 
    on f.id = gif.film_id
order by g.id asc
```

---
### Ссылка на диаграмму: 
[ER-диаграмма(java-filmorate-scheme)](https://miro.com/app/board/uXjVJvvDU5Q=/)