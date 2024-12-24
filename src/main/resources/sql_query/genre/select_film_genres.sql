SELECT
    g.id,
    g.name
FROM film_genre f_g
LEFT JOIN genres g
    ON f_g.genre_id = g.id
WHERE film_id = ?
ORDER BY g.id