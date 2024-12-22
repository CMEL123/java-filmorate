SELECT
    f.id,
    f.name,
    f.description,
    f.release_date,
    f.duration,
    f.rating_id,
    r.name as rating_name
FROM films f
LEFT JOIN ratings r
   ON f.rating_id = r.id
LEFT JOIN (
    select
        film_id,
        count(user_id) as likes
    from likes
    Group by film_id
) l
    ON f.id = l.film_id
Order by likes DESC
limit ?
