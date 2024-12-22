SELECT
    f.id,
    g.id,
    g.name
FROM films f
inner JOIN film_genre f_g
    ON f.id = f_g.film_id
inner JOIN genres g
    ON  f_g.genre_id = g.id
ORDER BY f.id