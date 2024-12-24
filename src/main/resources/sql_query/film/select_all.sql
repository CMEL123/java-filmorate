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
ORDER BY f.id